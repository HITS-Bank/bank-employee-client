package com.hits.bankemployee.client.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import com.hits.bankemployee.client.compose.component.ClientDetailsBlockDialog
import com.hits.bankemployee.client.event.ClientDetailsScreenEffect
import com.hits.bankemployee.client.event.ClientDetailsScreenEvent
import com.hits.bankemployee.client.model.ClientDetailsListItem
import com.hits.bankemployee.client.viewmodel.ClientDetailsScreenViewModel
import com.hits.bankemployee.core.presentation.common.LocalSnackbarController
import com.hits.bankemployee.core.presentation.common.component.ErrorContent
import com.hits.bankemployee.core.presentation.common.component.ListItem
import com.hits.bankemployee.core.presentation.common.component.ListItemEnd
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.LoadingContent
import com.hits.bankemployee.core.presentation.common.component.LoadingContentOverlay
import com.hits.bankemployee.core.presentation.common.component.PaginationErrorContent
import com.hits.bankemployee.core.presentation.common.component.PaginationLoadingContent
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.noRippleClickable
import com.hits.bankemployee.core.presentation.common.observeWithLifecycle
import com.hits.bankemployee.core.presentation.common.rememberCallback
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.core.presentation.theme.S14_W400
import com.hits.bankemployee.core.presentation.theme.S22_W400
import com.hits.bankemployee.core.presentation.theme.S24_W600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailsScreen(viewModel: ClientDetailsScreenViewModel) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            ClientDetailsScreenEffect.ShowBlockError -> snackbarController.show("Ошибка блокировки клиента")
            ClientDetailsScreenEffect.ShowUnblockError -> snackbarController.show("Ошибка разблокировки клиента")
        }
    }

    if (state.getIfSuccess()?.isDialogVisible == true) {
        ClientDetailsBlockDialog(state.getIfSuccess()?.client?.isBlocked ?: false, onEvent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.getIfSuccess()?.client?.fullName ?: "",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                            .noRippleClickable { onEvent(ClientDetailsScreenEvent.NavigateBack) },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(ClientDetailsScreenEvent.FabClicked) },
                text = { Text(text = state.getIfSuccess()?.fabText ?: "") },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            state.getIfSuccess()?.fabIconResId ?: 0
                        ),
                        contentDescription = null,
                    )
                },
                contentColor = MaterialTheme.colorScheme.primary,
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
                                    ClientDetailsListItem.AccountsHeader -> Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = "Счета",
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    ClientDetailsListItem.LoansHeader -> Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = "Кредиты",
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    is ClientDetailsListItem.BankAccountModel -> ListItem(
                                        modifier = Modifier.clickable {
                                            onEvent(
                                                ClientDetailsScreenEvent.BankAccountClicked(
                                                    number = item.number,
                                                    balance = item.balance,
                                                    status = item.status,
                                                )
                                            )
                                        },
                                        icon = ListItemIcon.Vector(iconResId = R.drawable.ic_bank_account),
                                        title = item.number,
                                        subtitle = item.description,
                                        end = ListItemEnd.Chevron,
                                        subtitleTextStyle = S14_W400.copy(color = item.descriptionColor),
                                    )

                                    is ClientDetailsListItem.LoanModel -> ListItem(
                                        modifier = Modifier.clickable {
                                            onEvent(
                                                ClientDetailsScreenEvent.LoanClicked(item.number)
                                            )
                                        },
                                        icon = ListItemIcon.Vector(iconResId = R.drawable.ic_loan),
                                        title = item.number,
                                        subtitle = item.description,
                                        end = ListItemEnd.Chevron,
                                        subtitleTextStyle = S14_W400.copy(color = item.descriptionColor),
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
                )

                else -> Unit
            }
        }
    }

    if (state.getIfSuccess()?.isPerformingAction == true) {
        LoadingContentOverlay()
    }
}