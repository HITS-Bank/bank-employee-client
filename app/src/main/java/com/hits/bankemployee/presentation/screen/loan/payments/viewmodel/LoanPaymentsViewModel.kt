package com.hits.bankemployee.presentation.screen.loan.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.presentation.navigation.LoanDetails
import com.hits.bankemployee.presentation.screen.loan.payments.event.LoanPaymentsEvent
import com.hits.bankemployee.presentation.screen.loan.payments.mapper.LoanPaymentsMapper
import com.hits.bankemployee.presentation.screen.loan.payments.model.LoanPaymentsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult

@HiltViewModel(assistedFactory = LoanPaymentsViewModel.Factory::class)
class LoanPaymentsViewModel @AssistedInject constructor(
    @Assisted private val loanId: String,
    private val navigationManager: NavigationManager,
    private val loanInteractor: LoanInteractor,
    private val mapper: LoanPaymentsMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<BankUiState<LoanPaymentsState>>(BankUiState.Loading)
    val state = _state.asStateFlow()

    fun onEvent(event: LoanPaymentsEvent) {
        when (event) {
            LoanPaymentsEvent.Back -> navigationManager.back()
            LoanPaymentsEvent.LoanInfoClick -> navigationManager.forwardWithCallbackResult(
                LoanDetails.destinationWithArgs(loanId)
            ) {
                refreshPayments()
            }
            LoanPaymentsEvent.Refresh -> refreshPayments()
        }
    }

    private fun refreshPayments() {
        viewModelScope.launch {
            loanInteractor.getLoanPayments(loanId).collect { paymentsState ->
                when (paymentsState) {
                    State.Loading -> {
                        _state.update { BankUiState.Loading }
                    }
                    is State.Error -> {
                        _state.update { BankUiState.Error(paymentsState.throwable) }
                    }
                    is State.Success -> {
                        val payments = paymentsState.data
                        _state.update {
                            BankUiState.Ready(
                                LoanPaymentsState(payments = payments.map(mapper::map))
                            )
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(loanId: String): LoanPaymentsViewModel
    }
}