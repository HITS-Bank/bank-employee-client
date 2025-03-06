package com.hits.bankemployee.loan.tariff.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.common.dropFirstBlank
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.common.map
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.core.domain.interactor.LoanInteractor
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEffect
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.loan.tariff.mapper.TariffsScreenModelMapper
import com.hits.bankemployee.loan.tariff.model.TariffModel
import com.hits.bankemployee.loan.tariff.model.TariffsPaginationState
import com.hits.bankemployee.loan.tariff.model.TariffsScreenDialogState
import com.hits.bankemployee.loan.tariff.model.getCreateTariff
import com.hits.bankemployee.loan.tariff.model.getDeleteTariff
import com.hits.bankemployee.loan.tariff.model.toDomain
import com.hits.bankemployee.loan.tariff.model.updateIfCreateTariff
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TariffsScreenViewModel(
    private val loanInteractor: LoanInteractor,
    private val mapper: TariffsScreenModelMapper,
) : PaginationViewModel<TariffModel, TariffsPaginationState>(BankUiState.Ready(TariffsPaginationState.EMPTY)) {

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
                viewModelScope.launch {
                    val dialogState = state.getIfSuccess()?.dialogState?.getCreateTariff()
                    if (dialogState == null) {
                        _effects.emit(TariffsScreenEffect.ShowTariffCreateError)
                        return@launch
                    }
                    loanInteractor.createLoanTariff(mapper.map(dialogState)).collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = true,
                                    )
                                }
                            }
                            is State.Error -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = false,
                                    )
                                }
                                _effects.emit(TariffsScreenEffect.ShowTariffCreateError)
                            }
                            is State.Success -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        dialogState = TariffsScreenDialogState.None,
                                        isPerformingAction = false,
                                    )
                                }
                                onPaginationEvent(PaginationEvent.Reload)
                            }
                        }
                    }
                }
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
                viewModelScope.launch {
                    val dialogState = state.getIfSuccess()?.dialogState?.getDeleteTariff()
                    if (dialogState == null) {
                        _effects.emit(TariffsScreenEffect.ShowTariffDeleteError)
                        return@launch
                    }
                    loanInteractor.deleteLoanTariff(dialogState.tariffId).collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = true,
                                    )
                                }
                            }
                            is State.Error -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = false,
                                    )
                                }
                                _effects.emit(TariffsScreenEffect.ShowTariffDeleteError)
                            }
                            is State.Success -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        dialogState = TariffsScreenDialogState.None,
                                        isPerformingAction = false,
                                    )
                                }
                                onPaginationEvent(PaginationEvent.Reload)
                            }
                        }
                    }
                }
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

    override fun getNextPageContents(pageNumber: Int): Flow<State<List<TariffModel>>> {
        val pageInfo = PageInfo(
            pageNumber = pageNumber,
            pageSize = state.getIfSuccess()?.pageSize ?: PAGE_SIZE,
        )
        return loanInteractor.getLoanTariffs(
            pageInfo = pageInfo,
            sortingProperty = state.getIfSuccess()?.sortingProperty?.toDomain() ?: LoanTariffSortingProperty.NAME,
            sortingOrder = state.getIfSuccess()?.sortingOrder?.toDomain() ?: LoanTariffSortingOrder.DESCENDING,
            query = state.getIfSuccess()?.query?.takeIf { it.isNotBlank() },
        ).map { state ->
            state.map { list -> list.map { entity -> mapper.map(entity) } }
        }
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