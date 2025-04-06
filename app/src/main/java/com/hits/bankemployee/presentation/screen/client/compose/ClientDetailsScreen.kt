package com.hits.bankemployee.presentation.screen.client.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import com.hits.bankemployee.presentation.screen.client.compose.component.ClientDetailsBlockDialog
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEffect
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEvent
import com.hits.bankemployee.presentation.screen.client.model.ClientDetailsListItem
import com.hits.bankemployee.presentation.screen.client.viewmodel.ClientDetailsScreenViewModel
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemEnd
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.component.PaginationErrorContent
import ru.hitsbank.bank_common.presentation.common.component.PaginationLoadingContent
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.noRippleClickable
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationReloadState
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.reloadState
import ru.hitsbank.bank_common.presentation.pagination.rememberPaginationListState
import ru.hitsbank.bank_common.presentation.theme.S14_W400
import ru.hitsbank.bank_common.presentation.theme.S22_W400
import ru.hitsbank.bank_common.presentation.theme.S24_W600

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
                                    ClientDetailsListItem.UserInfoHeader -> Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = "Информация о пользователе",
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    is ClientDetailsListItem.RolesModel -> ListItem(
                                        icon = ListItemIcon.None,
                                        title = item.rolesText,
                                        subtitle = "Роли",
                                        end = ListItemEnd.None,
                                        divider = Divider.None,
                                        padding = PaddingValues(vertical = 12.dp),
                                    )
                                    ClientDetailsListItem.IsBlockedModel -> ListItem(
                                        icon = ListItemIcon.None,
                                        title = "Заблокирован",
                                        subtitle = "Статус",
                                        end = ListItemEnd.None,
                                        divider = Divider.None,
                                        padding = PaddingValues(vertical = 12.dp),
                                    )
                                    is ClientDetailsListItem.LoanRatingModel -> ListItem(
                                        icon = ListItemIcon.None,
                                        title = item.rating,
                                        subtitle = "Кредитный рейтинг",
                                        end = ListItemEnd.None,
                                        divider = Divider.None,
                                        padding = PaddingValues(vertical = 12.dp),
                                    )

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
                                                    id = item.id,
                                                    number = item.number,
                                                    balance = item.balance,
                                                    currencyCode = item.currencyCode,
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
                                                ClientDetailsScreenEvent.LoanClicked(item.id)
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
                    onBack = {
                        onEvent(ClientDetailsScreenEvent.NavigateBack)
                    }
                )

                else -> Unit
            }
        }
    }

    if (state.getIfSuccess()?.isPerformingAction == true) {
        LoadingContentOverlay()
    }
}