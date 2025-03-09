package com.hits.bankemployee.presentation.screen.client.compose.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEvent

@Composable
fun ClientDetailsBlockDialog(
    isBlocked: Boolean,
    onEvent: (ClientDetailsScreenEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(ClientDetailsScreenEvent.DialogDismissed) },
        confirmButton = {
            Button(onClick = { onEvent(ClientDetailsScreenEvent.DialogConfirmed) }) {
                Text(text = if (isBlocked) "Разблокировать" else "Заблокировать")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(ClientDetailsScreenEvent.DialogDismissed) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "${ if (isBlocked) "Разблокировка" else "Блокировка" } пользователя")
        },
        text = {
            Text(text = "Вы уверены, что хотите ${ if (isBlocked) "разблокировать" else "заблокировать" } этого клиента?")
        }
    )
}