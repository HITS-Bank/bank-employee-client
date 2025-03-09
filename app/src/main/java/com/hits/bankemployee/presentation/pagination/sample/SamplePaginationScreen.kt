package com.hits.bankemployee.presentation.pagination.sample

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.presentation.common.component.ErrorContent
import com.hits.bankemployee.presentation.common.component.ListItem
import com.hits.bankemployee.presentation.common.component.ListItemIcon
import com.hits.bankemployee.presentation.common.component.LoadingContent
import com.hits.bankemployee.presentation.common.component.PaginationErrorContent
import com.hits.bankemployee.presentation.common.component.PaginationLoadingContent
import com.hits.bankemployee.presentation.common.getIfSuccess
import com.hits.bankemployee.presentation.pagination.PaginationEvent
import com.hits.bankemployee.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.presentation.pagination.PaginationState
import com.hits.bankemployee.presentation.pagination.reloadState
import com.hits.bankemployee.presentation.pagination.rememberPaginationListState

@Composable
fun SamplePaginationScreen(viewModel: SamplePaginationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)

    Crossfade(targetState = state.getIfSuccess()?.reloadState) { reloadState ->
        when (reloadState) {
            PaginationReloadState.Idle -> {
                LazyColumn(state = listState) {
                    state.getIfSuccess()?.data?.let { data ->
                        items(data) { item ->
                            ListItem(
                                icon = ListItemIcon.SingleChar('Я'),
                                title = "Item $item",
                                subtitle = "Page ${(item - 1) / (state.getIfSuccess()?.pageSize ?: 1)}",
                            )
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
}