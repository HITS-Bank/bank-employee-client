package com.hits.bankemployee.users.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.core.presentation.common.component.ErrorContent
import com.hits.bankemployee.core.presentation.common.component.ListItem
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.LoadingContent
import com.hits.bankemployee.core.presentation.common.component.PaginationErrorContent
import com.hits.bankemployee.core.presentation.common.component.PaginationLoadingContent
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.users.viewmodel.UserListViewModel

@Composable
fun UserList(viewModel: UserListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)

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
                        items(data) { item ->
                            val backgroundColor =
                                if (item.isBlocked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
                            val charColor =
                                if (item.isBlocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                            val subtitle =
                                if (item.isBlocked) "Заблокирован" else item.role.title
                            ListItem(
                                icon = ListItemIcon.SingleChar(
                                    char = item.firstName[0],
                                    backgroundColor = backgroundColor,
                                    charColor = charColor,
                                ),
                                title = "${item.firstName} ${item.lastName}",
                                subtitle = subtitle,
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