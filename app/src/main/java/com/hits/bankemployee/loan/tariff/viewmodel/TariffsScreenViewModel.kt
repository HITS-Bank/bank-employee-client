package com.hits.bankemployee.loan.tariff.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.common.dropFirstBlank
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEffect
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.loan.tariff.model.TariffsScreenDialogState
import com.hits.bankemployee.loan.tariff.model.TariffModel
import com.hits.bankemployee.loan.tariff.model.TariffsPaginationState
import com.hits.bankemployee.loan.tariff.model.updateIfCreateTariff
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.UUID

class TariffsScreenViewModel : PaginationViewModel<TariffModel, TariffsPaginationState>(BankUiState.Ready(TariffsPaginationState.EMPTY)) {

    private val _effects = MutableSharedFlow<TariffsScreenEffect>()
    val effects = _effects.asSharedFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        subscribeToQueryFlow()
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onEvent(event: TariffsScreenEvent) {
        when (event) {
            is TariffsScreenEvent.QueryChanged -> {
                _state.updateIfSuccess { state -> state.copy(queryDisplayText = event.query) }
                queryFlow.value = event.query
            }
            is TariffsScreenEvent.SortingOrderChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        sortingOrder = event.sortingOrder,
                        isSortingOrderMenuOpen = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            is TariffsScreenEvent.SortingPropertyChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        sortingProperty = event.sortingProperty,
                        isSortingPropertyMenuOpen = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            TariffsScreenEvent.TariffDialogClosed -> {
                _state.updateIfSuccess { state -> state.copy(dialogState = TariffsScreenDialogState.None) }
            }
            TariffsScreenEvent.TariffCreateClicked -> {
                _state.updateIfSuccess { state -> state.copy(dialogState = TariffsScreenDialogState.CreateTariff.EMPTY) }
            }
            is TariffsScreenEvent.TariffCreateInterestRateChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = state.dialogState.updateIfCreateTariff { dialogState ->
                            dialogState.copy(interestRate = event.interestRate)
                        },
                    )
                }
            }
            is TariffsScreenEvent.TariffCreateNameChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = state.dialogState.updateIfCreateTariff { dialogState ->
                            dialogState.copy(name = event.name)
                        },
                    )
                }
            }
            TariffsScreenEvent.TariffCreateConfirmed -> {
                //Loading
                _state.updateIfSuccess { state ->
                    state.copy(
                        isPerformingAction = true,
                    )
                }
                //Error
//                _state.updateIfSuccess { state ->
//                    state.copy(
//                        isPerformingAction = false,
//                    )
//                }
//                _effects.emit(TariffsScreenEffect.ShowTariffCreateError)
                //effect
                //Success
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = TariffsScreenDialogState.None,
                        isPerformingAction = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            is TariffsScreenEvent.TariffDeleteClicked -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = TariffsScreenDialogState.DeleteTariff(
                            tariffId = event.tariffId,
                            tariffName = event.tariffName,
                        )
                    )
                }
            }
            TariffsScreenEvent.TariffDeleteConfirmed -> {
                //Loading
                _state.updateIfSuccess { state ->
                    state.copy(
                        isPerformingAction = true,
                    )
                }
                //Error
//                _state.updateIfSuccess { state ->
//                    state.copy(
//                        isPerformingAction = false,
//                    )
//                }
//                _effects.emit(TariffsScreenEffect.ShowTariffDeleteError)
                //effect
                //Success
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = TariffsScreenDialogState.None,
                        isPerformingAction = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            TariffsScreenEvent.CloseSortingOrderMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingOrderMenuOpen = false) }
            }
            TariffsScreenEvent.CloseSortingPropertyMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingPropertyMenuOpen = false) }
            }
            TariffsScreenEvent.OpenSortingOrderMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingOrderMenuOpen = true) }
            }
            TariffsScreenEvent.OpenSortingPropertyMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingPropertyMenuOpen = true) }
            }
        }
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<TariffModel>>> = flow {
        emit(State.Loading)
        delay(2000)
        emit(State.Success(listOf(
            TariffModel(UUID.randomUUID().toString(), "Жесткий скам", "205.5%"),
            TariffModel(UUID.randomUUID().toString(), "Мягкий скам", "5.5%"),
            TariffModel(UUID.randomUUID().toString(), "Твёрдый скам", "55.5%"),
            TariffModel(UUID.randomUUID().toString(), "Нереальный скам", "1000.5%"),
            TariffModel(UUID.randomUUID().toString(), "Не скам", "0.5%"),
        )))
    }

    @OptIn(FlowPreview::class)
    private fun subscribeToQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(1000)
                .distinctUntilChanged()
                .dropFirstBlank()
                .collectLatest { query ->
                    _state.updateIfSuccess { state ->
                        state.copy(query = query)
                    }
                    onPaginationEvent(PaginationEvent.Reload)
                }
        }
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}