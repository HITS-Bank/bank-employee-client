package com.hits.bankemployee.users.compose.component

import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.component.ListItem
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.noRippleClickable
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.model.userlist.UserModel

enum class SwipePosition { Swiped, NotSwiped }

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun UserListItem(item: UserModel, onEvent: (UserListEvent) -> Unit) {
    val backgroundColor =
        if (item.isBlocked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
    val foregroundColor =
        if (item.isBlocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
    val subtitle =
        if (item.isBlocked) "Заблокирован" else item.role.title
    val icon =
        if (item.isBlocked) R.drawable.ic_unblock else R.drawable.ic_block

    val swipeOffset = with(LocalDensity.current) { 65.dp.toPx() }
    val velocityThreshold = with(LocalDensity.current) { 1000.dp.toPx() }
    val anchors = DraggableAnchors {
        SwipePosition.Swiped at -swipeOffset
        SwipePosition.NotSwiped at 0f
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = SwipePosition.NotSwiped,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.8f },
            velocityThreshold = { velocityThreshold },
            snapAnimationSpec = spring(
                dampingRatio = DampingRatioNoBouncy, stiffness = StiffnessHigh
            ),
            decayAnimationSpec = exponentialDecay()
        )
    }
    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor))
        Box(
            modifier = Modifier
                .noRippleClickable {
                    if (item.isBlocked) {
                        onEvent(UserListEvent.UnblockUser(item.id))
                    } else {
                        onEvent(UserListEvent.BlockUser(item.id))
                    }
                }
                .padding(horizontal = 19.dp)
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.size(27.dp),
                tint = foregroundColor,
            )
        }
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInteropFilter {
                    return@pointerInteropFilter false
                }
                .anchoredDraggable(
                    state = state, orientation = Orientation.Horizontal
                )
                .graphicsLayer {
                    translationX = state.requireOffset()
                }
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onEvent(UserListEvent.OpenClientDetails(item.id)) },
            icon = ListItemIcon.SingleChar(
                char = item.firstName[0],
                backgroundColor = backgroundColor,
                charColor = foregroundColor,
            ),
            title = "${item.firstName} ${item.lastName}",
            subtitle = subtitle,
        )
    }
}