package com.hits.bankemployee.users.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.LocalSnackbarController
import com.hits.bankemployee.core.presentation.common.component.LoadingContentOverlay
import com.hits.bankemployee.core.presentation.common.component.SearchTextField
import com.hits.bankemployee.users.compose.component.CreateUserDialog
import com.hits.bankemployee.users.compose.component.UsersScreenPager
import com.hits.bankemployee.users.effect.UsersScreenEffect
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.event.UsersScreenEvent
import com.hits.bankemployee.users.model.CreateUserDialogState
import com.hits.bankemployee.users.model.UsersTab
import com.hits.bankemployee.users.viewmodel.UserListViewModel
import com.hits.bankemployee.users.viewmodel.UsersScreenViewModel

@Composable
fun UsersScreen(viewModel: UsersScreenViewModel = viewModel()) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val clientViewModel: UserListViewModel = viewModel()
    val employeeViewModel: UserListViewModel = viewModel()

    when (val dialogState = state.createUserDialogState) {
        CreateUserDialogState.Hidden -> Unit
        is CreateUserDialogState.Shown -> CreateUserDialog(
            selectedTab = state.selectedTab,
            state = dialogState,
            onEvent = viewModel::onEvent,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is UsersScreenEffect.ReloadUsers -> {
                    clientViewModel.onEvent(UserListEvent.Reload(effect.query))
                    employeeViewModel.onEvent(UserListEvent.Reload(effect.query))
                }
                UsersScreenEffect.ShowUserCreationError -> snackbarController.show("Ошибка создания пользователя")
            }
        }
    }

    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTextField(
                text = state.query,
                onTextChanged = { viewModel.onEvent(UsersScreenEvent.QueryChanged(it)) },
                placeholder = "ФИО",
            )
            UsersScreenPager(viewModel::onEvent) { tab ->
                val userListViewModel = when (tab) {
                    UsersTab.CLIENTS -> clientViewModel
                    UsersTab.EMPLOYEES -> employeeViewModel
                }
                UserList(userListViewModel)
            }
        }
        FloatingActionButton(
            onClick = { viewModel.onEvent(UsersScreenEvent.CreateUserDialogOpen) },
            modifier = Modifier.padding(18.dp).align(Alignment.BottomEnd),
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
    }

    if (state.isCreatingUser) {
        LoadingContentOverlay()
    }
}