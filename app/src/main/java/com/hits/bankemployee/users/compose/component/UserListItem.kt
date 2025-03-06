package com.hits.bankemployee.users.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.SwipeableInfo
import com.hits.bankemployee.core.presentation.common.component.SwipeableListItem
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.model.userlist.UserModel

enum class SwipePosition { Swiped, NotSwiped }

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

    SwipeableListItem(
        modifier = Modifier.clickable { onEvent(UserListEvent.OpenClientDetails(item.id)) },
        icon = ListItemIcon.SingleChar(
            char = item.firstName[0],
            backgroundColor = backgroundColor,
            charColor = foregroundColor,
        ),
        title = "${item.firstName} ${item.lastName}",
        subtitle = subtitle,
        swipeableInfo = SwipeableInfo(
            backgroundColor = backgroundColor,
            iconColor = foregroundColor,
            iconResId = icon,
            onIconClick = {
                if (item.isBlocked) {
                    onEvent(UserListEvent.UnblockUser(item.id))
                } else {
                    onEvent(UserListEvent.BlockUser(item.id))
                }
            }
        ),
    )
}