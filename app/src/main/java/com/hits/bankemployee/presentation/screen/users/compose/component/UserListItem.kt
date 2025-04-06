package com.hits.bankemployee.presentation.screen.users.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hits.bankemployee.presentation.screen.users.event.UserListEvent
import com.hits.bankemployee.presentation.screen.users.model.userlist.UserModel
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.SwipeableInfo
import ru.hitsbank.bank_common.presentation.common.component.SwipeableListItem

@Composable
fun UserListItem(item: UserModel, onEvent: (UserListEvent) -> Unit) {
    SwipeableListItem(
        modifier = Modifier.clickable {
            onEvent(
                UserListEvent.OpenClientDetails(
                    item.id,
                    item.fullName,
                    item.isBlocked,
                    item.roles,
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