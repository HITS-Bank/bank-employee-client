package com.hits.bankemployee.users.compose.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hits.bankemployee.users.event.UserListEvent

@Composable
fun BlockDialog(
    onEvent: (UserListEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(UserListEvent.CloseBlockDialog) },
        confirmButton = {
            Button(onClick = { onEvent(UserListEvent.ConfirmBlock) }) {
                Text(text = "Заблокировать")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(UserListEvent.CloseBlockDialog) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "Блокировка пользователя")
        },
        text = {
            Text(text = "Вы уверены, что хотите заблокировать этого пользователя?")
        }
    )
}

@Composable
fun UnblockDialog(
    onEvent: (UserListEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(UserListEvent.CloseBlockDialog) },
        confirmButton = {
            Button(onClick = { onEvent(UserListEvent.ConfirmUnblock) }) {
                Text(text = "Разблокировать")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(UserListEvent.CloseBlockDialog) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "Разблокировка пользователя")
        },
        text = {
            Text(text = "Вы уверены, что хотите разблокировать этого пользователя?")
        }
    )
}