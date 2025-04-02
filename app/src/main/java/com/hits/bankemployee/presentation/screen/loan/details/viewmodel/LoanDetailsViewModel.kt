package com.hits.bankemployee.presentation.screen.loan.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.presentation.navigation.BankAccountDetails
import com.hits.bankemployee.presentation.screen.loan.details.event.LoanDetailsEvent
import com.hits.bankemployee.presentation.screen.loan.details.mapper.LoanDetailsMapper
import com.hits.bankemployee.presentation.screen.loan.details.model.LoanDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult

@HiltViewModel(assistedFactory = LoanDetailsViewModel.Factory::class)
class LoanDetailsViewModel @AssistedInject constructor(
    @Assisted private val loanId: String,
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
                        bankAccountId = event.accountId,
                    )
                ) {
                    forceReloadLoanDetails(loanId)
                }
            }

            LoanDetailsEvent.Back -> {
                navigationManager.back()
            }
        }
    }

    private fun loadLoanDetails() {
        forceReloadLoanDetails(loanId)
    }

    private fun forceReloadLoanDetails(loanId: String) {
        val loanEntityRequest = loanInteractor.getLoanById(loanId)
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

    @AssistedFactory
    interface Factory {
        fun create(loanId: String): LoanDetailsViewModel
    }
}