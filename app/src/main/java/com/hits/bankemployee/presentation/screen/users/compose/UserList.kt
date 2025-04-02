package com.hits.bankemployee.presentation.screen.users.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.presentation.screen.users.compose.component.BlockDialog
import com.hits.bankemployee.presentation.screen.users.compose.component.UnblockDialog
import com.hits.bankemployee.presentation.screen.users.compose.component.UserListItem
import com.hits.bankemployee.presentation.screen.users.event.UserListEffect
import com.hits.bankemployee.presentation.screen.users.model.UsersTab
import com.hits.bankemployee.presentation.screen.users.viewmodel.UserListViewModel
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.component.PaginationErrorContent
import ru.hitsbank.bank_common.presentation.common.component.PaginationLoadingContent
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationReloadState
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.reloadState
import ru.hitsbank.bank_common.presentation.pagination.rememberPaginationListState

@Composable
fun UserList(
    tab: UsersTab,
    viewModel: UserListViewModel = hiltViewModel<UserListViewModel, UserListViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(tab.role)
        }
    ),
) {
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)

    viewModel.effects.observeWithLifecycle { event ->
        when (event) {
            is UserListEffect.ShowBlockError -> snackbarController.show("Ошибка блокировки пользователя")
            is UserListEffect.ShowUnblockError -> snackbarController.show("Ошибка разблокировки пользователя")
        }
    }

    if (state.getIfSuccess()?.blockUserId != null) {
        BlockDialog(onEvent)
    }
    if (state.getIfSuccess()?.unblockUserId != null) {
        UnblockDialog(onEvent)
    }

    Crossfade(
        modifier = Modifier.fillMaxSize(),
        targetState = state.getIfSuccess()?.reloadState,
    ) { reloadState ->
        when (reloadState) {
            PaginationReloadState.Idle -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(top = 16.dp),
                ) {
                    state.getIfSuccess()?.data?.let { data ->
                        items(data, key = { item -> item.id }) { item ->
                            UserListItem(item, onEvent)
                        }
                    }

                    when (state.getIfSuccess()?.paginationState) {
                        PaginationState.Loading -> item {
                            PaginationLoadingContent()
                        }

                        PaginationState.Error -> item {
                            PaginationErrorContent {
                                onPaginationEvent(PaginationEvent.LoadNextPage)
                            }
                        }

                        else -> Unit
                    }
                }
            }

            PaginationReloadState.Reloading -> LoadingContent()

            PaginationReloadState.Error -> ErrorContent(
                onReload = {
                    onPaginationEvent(PaginationEvent.Reload)
                },
            )

            else -> Unit
        }
    }

    if (state.getIfSuccess()?.isPerformingAction == true) {
        LoadingContentOverlay()
    }
}