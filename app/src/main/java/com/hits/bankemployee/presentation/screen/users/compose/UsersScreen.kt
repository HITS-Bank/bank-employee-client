package com.hits.bankemployee.presentation.screen.users.compose

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.R
import com.hits.bankemployee.presentation.screen.users.compose.component.CreateUserDialog
import com.hits.bankemployee.presentation.screen.users.compose.component.UsersScreenPager
import com.hits.bankemployee.presentation.screen.users.event.UserListEvent
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEffect
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEvent
import com.hits.bankemployee.presentation.screen.users.model.CreateUserDialogState
import com.hits.bankemployee.presentation.screen.users.model.UserRole
import com.hits.bankemployee.presentation.screen.users.viewmodel.UserListViewModel
import com.hits.bankemployee.presentation.screen.users.viewmodel.UsersScreenViewModel
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.component.SearchTextField
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback

@Composable
fun UsersScreen(viewModel: UsersScreenViewModel = hiltViewModel()) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val clientViewModel: UserListViewModel = hiltViewModel<UserListViewModel, UserListViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(UserRole.CLIENT)
        }
    )
    val employeeViewModel: UserListViewModel = hiltViewModel<UserListViewModel, UserListViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(UserRole.EMPLOYEE)
        }
    )

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