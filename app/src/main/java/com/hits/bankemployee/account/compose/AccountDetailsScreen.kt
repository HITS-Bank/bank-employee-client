package com.hits.bankemployee.account.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.R
import com.hits.bankemployee.account.event.AccountDetailsScreenEvent
import com.hits.bankemployee.account.model.AccountDetailsListItem
import com.hits.bankemployee.account.viewmodel.AccountDetailsScreenViewModel
import com.hits.bankemployee.core.presentation.common.component.Divider
import com.hits.bankemployee.core.presentation.common.component.ErrorContent
import com.hits.bankemployee.core.presentation.common.component.ListItem
import com.hits.bankemployee.core.presentation.common.component.ListItemEnd
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.LoadingContent
import com.hits.bankemployee.core.presentation.common.component.PaginationErrorContent
import com.hits.bankemployee.core.presentation.common.component.PaginationLoadingContent
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.noRippleClickable
import com.hits.bankemployee.core.presentation.common.rememberCallback
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.core.presentation.theme.S22_W400
import com.hits.bankemployee.core.presentation.theme.S24_W600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(viewModel: AccountDetailsScreenViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Счет",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                            .noRippleClickable { onEvent(AccountDetailsScreenEvent.NavigateBack) },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
    ) { padding ->
        Crossfade(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            targetState = state.getIfSuccess()?.reloadState,
        ) { reloadState ->
            when (reloadState) {
                PaginationReloadState.Idle -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                    ) {
                        state.getIfSuccess()?.data?.let { data ->
                            items(data) { item ->
                                when (item) {
                                    AccountDetailsListItem.AccountDetailsHeader -> Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = "Информация о счёте",
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    AccountDetailsListItem.OperationHistoryHeader -> Text(
                                        modifier = Modifier.padding(
                                            top = 32.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 16.dp,
                                        ),
                                        text = "История операций",
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    is AccountDetailsListItem.AccountDetailsProperty -> ListItem(
                                        icon = ListItemIcon.None,
                                        divider = Divider.None,
                                        title = item.value,
                                        subtitle = item.name,
                                        padding = PaddingValues(vertical = 12.dp),
                                    )
                                    is AccountDetailsListItem.OperationHistoryItem -> ListItem(
                                        icon = ListItemIcon.SingleChar(
                                            char = '₽',
                                            backgroundColor = item.iconBackground,
                                            charColor = item.iconColor,
                                        ),
                                        end = ListItemEnd.OperationAmount(
                                            amount = item.amount,
                                            amountColor = item.amountColor,
                                        ),
                                        divider = Divider.None,
                                        title = item.operationTitle,
                                        subtitle = item.date,
                                    )
                                }
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
                    onBack = {
                        onEvent(AccountDetailsScreenEvent.NavigateBack)
                    }
                )

                else -> Unit
            }
        }
    }
}