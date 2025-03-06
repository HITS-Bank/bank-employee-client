package com.hits.bankemployee.users.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.core.presentation.common.LocalSnackbarController
import com.hits.bankemployee.core.presentation.common.component.ErrorContent
import com.hits.bankemployee.core.presentation.common.component.LoadingContent
import com.hits.bankemployee.core.presentation.common.component.LoadingContentOverlay
import com.hits.bankemployee.core.presentation.common.component.PaginationErrorContent
import com.hits.bankemployee.core.presentation.common.component.PaginationLoadingContent
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.observeWithLifecycle
import com.hits.bankemployee.core.presentation.common.rememberCallback
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.users.compose.component.BlockDialog
import com.hits.bankemployee.users.compose.component.UnblockDialog
import com.hits.bankemployee.users.compose.component.UserListItem
import com.hits.bankemployee.users.event.UserListEffect
import com.hits.bankemployee.users.model.UsersTab
import com.hits.bankemployee.users.viewmodel.UserListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun UserList(tab: UsersTab, viewModel: UserListViewModel = koinViewModel(named(tab.role.name))) {
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