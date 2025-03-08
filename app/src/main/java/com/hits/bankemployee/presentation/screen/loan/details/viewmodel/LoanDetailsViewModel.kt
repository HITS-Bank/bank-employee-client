package com.hits.bankemployee.presentation.screen.loan.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.presentation.common.BankUiState
import com.hits.bankemployee.presentation.navigation.BankAccountDetails
import com.hits.bankemployee.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.presentation.navigation.base.back
import com.hits.bankemployee.presentation.navigation.base.forwardWithCallbackResult
import com.hits.bankemployee.presentation.screen.loan.details.event.LoanDetailsEvent
import com.hits.bankemployee.presentation.screen.loan.details.mapper.LoanDetailsMapper
import com.hits.bankemployee.presentation.screen.loan.details.model.LoanDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanDetailsViewModel(
    private val loanNumber: String,
    private val loanInteractor: LoanInteractor,
    private val mapper: LoanDetailsMapper,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state = MutableStateFlow<BankUiState<LoanDetailsState>>(BankUiState.Loading)
    val state = _state.asStateFlow()

    init {
        loadLoanDetails()
    }

    fun onEvent(event: LoanDetailsEvent) {
        when (event) {
            is LoanDetailsEvent.OpenBankAccount -> {
                navigationManager.forwardWithCallbackResult(
                    BankAccountDetails.withArgs(
                        bankAccountNumber = event.accountNumber,
                    )
                ) {
                    forceReloadLoanDetails(loanNumber)
                }
            }

            LoanDetailsEvent.Back -> {
                navigationManager.back()
            }
        }
    }

    private fun loadLoanDetails() {
        forceReloadLoanDetails(loanNumber)
    }

    private fun forceReloadLoanDetails(loanNumber: String) {
        val loanEntityRequest = loanInteractor.getLoanByNumber(loanNumber)
        viewModelScope.launch {
            loanEntityRequest.collectLatest { state ->
                _state.update {
                    when (state) {
                        is State.Error -> BankUiState.Error()
                        State.Loading -> BankUiState.Loading
                        is State.Success -> BankUiState.Ready(
                            model = LoanDetailsState.default(mapper.map(state.data))
                        )
                    }
                }
            }
        }
    }
}