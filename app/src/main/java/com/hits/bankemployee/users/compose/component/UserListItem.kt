package com.hits.bankemployee.users.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.SwipeableInfo
import com.hits.bankemployee.core.presentation.common.component.SwipeableListItem
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.model.userlist.UserModel

@Composable
fun UserListItem(item: UserModel, onEvent: (UserListEvent) -> Unit) {
    SwipeableListItem(
        modifier = Modifier.clickable {
            onEvent(
                UserListEvent.OpenClientDetails(
                    item.id,
                    item.fullName,
                    item.isBlocked,
                )
            )
        },
        icon = ListItemIcon.SingleChar(
            char = item.fullName[0],
            backgroundColor = item.backgroundColor,
            charColor = item.foregroundColor,
        ),
        title = item.fullName,
        subtitle = item.status,
        swipeableInfo = SwipeableInfo(
            backgroundColor = item.backgroundColor,
            iconColor = item.foregroundColor,
            iconResId = item.actionIconResId,
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