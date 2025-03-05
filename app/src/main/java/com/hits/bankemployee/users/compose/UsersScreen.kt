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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.LocalSnackbarController
import com.hits.bankemployee.core.presentation.common.component.LoadingContentOverlay
import com.hits.bankemployee.core.presentation.common.component.SearchTextField
import com.hits.bankemployee.core.presentation.common.observeWithLifecycle
import com.hits.bankemployee.core.presentation.common.rememberCallback
import com.hits.bankemployee.users.compose.component.CreateUserDialog
import com.hits.bankemployee.users.compose.component.UsersScreenPager
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.event.UsersScreenEffect
import com.hits.bankemployee.users.event.UsersScreenEvent
import com.hits.bankemployee.users.model.CreateUserDialogState
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.viewmodel.UserListViewModel
import com.hits.bankemployee.users.viewmodel.UsersScreenViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun UsersScreen(viewModel: UsersScreenViewModel = koinViewModel()) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val clientViewModel: UserListViewModel = koinViewModel(named(UserRole.CLIENT.name))
    val employeeViewModel: UserListViewModel = koinViewModel(named(UserRole.EMPLOYEE.name))

    when (val dialogState = state.createUserDialogState) {
        CreateUserDialogState.Hidden -> Unit
        is CreateUserDialogState.Shown -> CreateUserDialog(
            selectedTab = state.selectedTab,
            state = dialogState,
            onEvent = viewModel::onEvent,
        )
    }

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            is UsersScreenEffect.ReloadUsers -> {
                clientViewModel.onEvent(UserListEvent.Reload(effect.query))
                employeeViewModel.onEvent(UserListEvent.Reload(effect.query))
            }
            UsersScreenEffect.ShowUserCreationError -> snackbarController.show("Ошибка создания пользователя")
        }
    }

    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTextField(
                text = state.query,
                onTextChanged = { onEvent(UsersScreenEvent.QueryChanged(it)) },
                placeholder = "ФИО",
            )
            UsersScreenPager(onEvent) { tab ->
                UserList(tab)
            }
        }
        FloatingActionButton(
            onClick = { onEvent(UsersScreenEvent.CreateUserDialogOpen) },
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