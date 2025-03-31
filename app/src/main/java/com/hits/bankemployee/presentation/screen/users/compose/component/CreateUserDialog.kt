package com.hits.bankemployee.presentation.screen.users.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEvent
import com.hits.bankemployee.presentation.screen.users.model.CreateUserDialogState
import com.hits.bankemployee.presentation.screen.users.model.UsersTab
import ru.hitsbank.bank_common.presentation.common.horizontalSpacer
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.bank_common.presentation.theme.S16_W400
import ru.hitsbank.bank_common.presentation.theme.S16_W500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserDialog(
    selectedTab: UsersTab,
    state: CreateUserDialogState.Shown,
    onEvent: (UsersScreenEvent) -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onEvent(UsersScreenEvent.CreateUserDialogClose) },
    ) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Создание ${selectedTab.creationTitle}",
                    style = S16_W500,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.model.firstName,
                    onValueChange = { onEvent(UsersScreenEvent.CreateUserFirstNameChanged(it)) },
                    label = {
                        Text(text = "Имя")
                    },
                    singleLine = true,
                    isError = !state.model.isFirstNameValid,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.model.lastName,
                    onValueChange = { onEvent(UsersScreenEvent.CreateUserLastNameChanged(it)) },
                    label = {
                        Text(text = "Фамилия")
                    },
                    singleLine = true,
                    isError = !state.model.isLastNameValid,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.model.email,
                    onValueChange = { onEvent(UsersScreenEvent.CreateUserEmailChanged(it)) },
                    label = {
                        Text(text = "Почта")
                    },
                    singleLine = true,
                    isError = !state.model.isEmailValid,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.model.password,
                    onValueChange = { onEvent(UsersScreenEvent.CreateUserPasswordChanged(it)) },
                    label = {
                        Text(text = "Пароль")
                    },
                    singleLine = true,
                    isError = !state.model.isPasswordValid,
                )
                16.dp.verticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { onEvent(UsersScreenEvent.CreateUserDialogClose) },
                    ) {
                        Text(
                            text = "Отмена",
                            style = S16_W400,
                        )
                    }
                    8.dp.horizontalSpacer()
                    Button(
                        onClick = { onEvent(UsersScreenEvent.CreateUser) },
                        enabled = state.model.createButtonEnabled,
                    ) {
                        Text(
                            text = "Создать",
                            style = S16_W400,
                        )
                    }
                }
            }
        }
    }
}