package com.hits.bankemployee.presentation.screen.loan.tariff.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.hits.bankemployee.presentation.screen.loan.tariff.compose.component.TariffCreateDialog
import com.hits.bankemployee.presentation.screen.loan.tariff.compose.component.TariffDeleteDialog
import com.hits.bankemployee.presentation.screen.loan.tariff.compose.component.TariffListItem
import com.hits.bankemployee.presentation.screen.loan.tariff.event.TariffsScreenEffect
import com.hits.bankemployee.presentation.screen.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.presentation.screen.loan.tariff.model.SortingOrder
import com.hits.bankemployee.presentation.screen.loan.tariff.model.SortingProperty
import com.hits.bankemployee.presentation.screen.loan.tariff.model.TariffsScreenDialogState
import com.hits.bankemployee.presentation.screen.loan.tariff.viewmodel.TariffsScreenViewModel
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.component.PaginationErrorContent
import ru.hitsbank.bank_common.presentation.common.component.PaginationLoadingContent
import ru.hitsbank.bank_common.presentation.common.component.SearchTextField
import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownField
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.horizontalSpacer
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationReloadState
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.reloadState
import ru.hitsbank.bank_common.presentation.pagination.rememberPaginationListState

@Composable
fun TariffsScreen(viewModel: TariffsScreenViewModel = hiltViewModel()) {
    val snackbarController = LocalSnackbarController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)
    val listState = rememberPaginationListState(viewModel)

    when (val dialogState = state.getIfSuccess()?.dialogState) {
        is TariffsScreenDialogState.CreateTariff -> TariffCreateDialog(dialogState, onEvent)
        is TariffsScreenDialogState.DeleteTariff -> TariffDeleteDialog(dialogState, onEvent)
        else -> Unit
    }

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            TariffsScreenEffect.ShowTariffCreateError -> snackbarController.show("Ошибка создания тарифа")
            TariffsScreenEffect.ShowTariffDeleteError -> snackbarController.show("Ошибка удаления тарифа")
        }
    }

    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTextField(
                text = state.getIfSuccess()?.queryDisplayText ?: "",
                onTextChanged = { onEvent(TariffsScreenEvent.QueryChanged(it)) },
                placeholder = "Название тарифа",
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                DropdownField(
                    items = SortingProperty.entries,
                    selectedItem = state.getIfSuccess()?.sortingProperty ?: SortingProperty.NAME,
                    onItemSelected = { onEvent(TariffsScreenEvent.SortingPropertyChanged(it)) },
                    isDropdownOpen = state.getIfSuccess()?.isSortingPropertyMenuOpen ?: false,
                    onOpenDropdown = { onEvent(TariffsScreenEvent.OpenSortingPropertyMenu) },
                    onCloseDropdown = { onEvent(TariffsScreenEvent.CloseSortingPropertyMenu) },
                    label = "Сортировать по",
                    modifier = Modifier.weight(1f),
                )
                16.dp.horizontalSpacer()
                DropdownField(
                    items = SortingOrder.entries,
                    selectedItem = state.getIfSuccess()?.sortingOrder ?: SortingOrder.DESCENDING,
                    onItemSelected = { onEvent(TariffsScreenEvent.SortingOrderChanged(it)) },
                    isDropdownOpen = state.getIfSuccess()?.isSortingOrderMenuOpen ?: false,
                    onOpenDropdown = { onEvent(TariffsScreenEvent.OpenSortingOrderMenu) },
                    onCloseDropdown = { onEvent(TariffsScreenEvent.CloseSortingOrderMenu) },
                    label = "Порядок",
                    modifier = Modifier.weight(1f),
                )
            }
            16.dp.verticalSpacer()
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = state.getIfSuccess()?.reloadState,
            ) { reloadState ->
                when (reloadState) {
                    PaginationReloadState.Idle -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                        ) {
                            state.getIfSuccess()?.data?.let { data ->
                                items(data, key = { item -> item.id }) { item ->
                                    TariffListItem(item, onEvent)
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
        FloatingActionButton(
            onClick = { onEvent(TariffsScreenEvent.TariffCreateClicked) },
            modifier = Modifier
                .padding(18.dp)
                .align(Alignment.BottomEnd),
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
    }

    if (state.getIfSuccess()?.isPerformingAction == true) {
        LoadingContentOverlay()
    }
}