package com.hits.bankemployee.users.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.users.compose.component.BlockDialog
import com.hits.bankemployee.users.compose.component.UnblockDialog
import com.hits.bankemployee.users.compose.component.UserListItem
import com.hits.bankemployee.users.effect.UserListEffect
import com.hits.bankemployee.users.viewmodel.UserListViewModel

@Composable
fun UserList(viewModel: UserListViewModel) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { event ->
            when (event) {
                is UserListEffect.ShowBlockError -> snackbarController.show("Ошибка блокировки пользователя")
                is UserListEffect.ShowUnblockError -> snackbarController.show("Ошибка разблокировки пользователя")
            }
        }
    }

    if (state.getIfSuccess()?.blockUserId != null) {
        BlockDialog(viewModel::onEvent)
    }
    if (state.getIfSuccess()?.unblockUserId != null) {
        UnblockDialog(viewModel::onEvent)
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
                            UserListItem(item, viewModel::onEvent)
                        }
                    }

                    when (state.getIfSuccess()?.paginationState) {
                        PaginationState.Loading -> item {
                            PaginationLoadingContent()
                        }

                        PaginationState.Error -> item {
                            PaginationErrorContent {
                                viewModel.onPaginationEvent(PaginationEvent.LoadNextPage)
                            }
                        }

                        else -> Unit
                    }
                }
            }

            PaginationReloadState.Reloading -> LoadingContent()
            PaginationReloadState.Error -> ErrorContent(
                onReload = {
                    viewModel.onPaginationEvent(PaginationEvent.Reload)
                },
                onBack = { },
            )

            else -> Unit
        }
    }

    if (state.getIfSuccess()?.isPerformingAction == true) {
        LoadingContentOverlay()
    }
}