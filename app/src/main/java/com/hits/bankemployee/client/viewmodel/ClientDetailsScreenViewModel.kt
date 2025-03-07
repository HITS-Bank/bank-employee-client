package com.hits.bankemployee.client.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.client.event.ClientDetailsScreenEffect
import com.hits.bankemployee.client.event.ClientDetailsScreenEvent
import com.hits.bankemployee.client.model.BankAccountStatus
import com.hits.bankemployee.client.model.ClientDetailsListItem
import com.hits.bankemployee.client.model.ClientDetailsPaginationState
import com.hits.bankemployee.client.model.ClientModel
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.interactor.ProfileInteractor
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.navigation.BankAccountDetails
import com.hits.bankemployee.core.presentation.navigation.LoanDetails
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.navigation.base.back
import com.hits.bankemployee.core.presentation.navigation.base.forwardWithCallbackResult
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ClientDetailsScreenViewModel(
    client: ClientModel,
    private val profileInteractor: ProfileInteractor,
    private val navigationManager: NavigationManager,
) : PaginationViewModel<ClientDetailsListItem, ClientDetailsPaginationState>(
    BankUiState.Ready(ClientDetailsPaginationState.empty(client))
) {
    private var bankAccountListLastPageNumber: Int? = null

    private val _effects = MutableSharedFlow<ClientDetailsScreenEffect>()
    val effects = _effects.asSharedFlow()

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onEvent(event: ClientDetailsScreenEvent) {
        when (event) {
            ClientDetailsScreenEvent.DialogConfirmed -> {
                if (_state.getIfSuccess()?.isPerformingAction == true) {
                    return
                }
                val clientId = _state.value.getIfSuccess()?.client?.id ?: return
                val isBlocked = _state.value.getIfSuccess()?.client?.isBlocked ?: return
                val flow = if (isBlocked) {
                    profileInteractor.unbanUser(clientId)
                } else {
                    profileInteractor.banUser(clientId)
                }
                viewModelScope.launch {
                    flow.collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = true)
                                }
                            }

                            is State.Error -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = false,
                                        isDialogVisible = false
                                    )
                                }
                                if (isBlocked) {
                                    _effects.emit(ClientDetailsScreenEffect.ShowUnblockError)
                                } else {
                                    _effects.emit(ClientDetailsScreenEffect.ShowBlockError)
                                }
                            }

                            is State.Success -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(
                                        isPerformingAction = false,
                                        isDialogVisible = false,
                                        client = oldState.client.copy(isBlocked = !isBlocked),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            ClientDetailsScreenEvent.DialogDismissed -> {
                if (_state.getIfSuccess()?.isPerformingAction == true) {
                    return
                }
                _state.updateIfSuccess { state ->
                    state.copy(isDialogVisible = false)
                }
            }

            ClientDetailsScreenEvent.FabClicked -> {
                _state.updateIfSuccess { state ->
                    state.copy(isDialogVisible = true)
                }
            }

            ClientDetailsScreenEvent.NavigateBack -> {
                navigationManager.back()
            }

            is ClientDetailsScreenEvent.LoanClicked -> {
                navigationManager.forwardWithCallbackResult(
                    LoanDetails.destinationWithArgs(event.number)
                ) {
                    onPaginationEvent(PaginationEvent.Reload)
                }
            }

            is ClientDetailsScreenEvent.BankAccountClicked -> {
                navigationManager.forwardWithCallbackResult(
                    BankAccountDetails.withArgs(event.number, event.balance, event.status.name)
                ) {
                    onPaginationEvent(PaginationEvent.Reload)
                }
            }
        }
    }

    override fun getNextPageContents(pageNumber: Int): Flow<State<List<ClientDetailsListItem>>> =
        flow {
            emit(State.Loading)
            if (pageNumber == 0) {
                bankAccountListLastPageNumber = null
            }
            val bankAccountPageNumber = bankAccountListLastPageNumber
            if (bankAccountPageNumber == null) {
                loadAccountsPage(pageNumber)
            } else {
                emit(getLoansPage(pageNumber - bankAccountPageNumber).last())
            }
        }

    private suspend fun FlowCollector<State<List<ClientDetailsListItem>>>.loadAccountsPage(
        pageNumber: Int
    ) {
        val pageResult = getBankAccountsPage(pageNumber).last()
        if (pageResult is State.Success) {
            val accountList = mutableListOf<ClientDetailsListItem>()
            if (pageNumber == 0) {
                accountList.add(ClientDetailsListItem.AccountsHeader)
            }
            accountList.addAll(pageResult.data)
            if (pageResult.data.size < PAGE_SIZE) {
                if (!tryLoadInitialLoanPage(pageNumber, accountList)) {
                    return
                }
            }
            emit(State.Success(accountList))
        } else {
            emit(pageResult)
        }
    }

    private suspend fun FlowCollector<State<List<ClientDetailsListItem>>>.tryLoadInitialLoanPage(
        pageNumber: Int,
        accountList: MutableList<ClientDetailsListItem>
    ): Boolean {
        bankAccountListLastPageNumber = pageNumber
        val loanPageResult = getLoansPage(0).last()
        if (loanPageResult is State.Success) {
            accountList.add(ClientDetailsListItem.LoansHeader)
            accountList.addAll(loanPageResult.data)
            return true
        } else {
            emit(loanPageResult)
            return false
        }
    }

    //TODO replace with interactor
    private fun getBankAccountsPage(pageNumber: Int): Flow<State<List<ClientDetailsListItem.BankAccountModel>>> =
        flow {
            emit(State.Loading)
            delay(1000)
            val accountList = when (pageNumber) {
                0 -> {
                    listOf(
                        ClientDetailsListItem.BankAccountModel(
                            number = "1234567890",
                            balance = "5000",
                            status = BankAccountStatus.OPEN,
                        ),
                        ClientDetailsListItem.BankAccountModel(
                            number = "0987654321",
                            balance = "0",
                            status = BankAccountStatus.CLOSED,
                        ),
                        ClientDetailsListItem.BankAccountModel(
                            number = "1357924680",
                            balance = "10000",
                            status = BankAccountStatus.BLOCKED,
                        ),
                        ClientDetailsListItem.BankAccountModel(
                            number = "2468135790",
                            balance = "20000",
                            status = BankAccountStatus.OPEN,
                        ),
                        ClientDetailsListItem.BankAccountModel(
                            number = "9876543210",
                            balance = "30000",
                            status = BankAccountStatus.OPEN,
                        ),
                    )
                }

                1 -> {
                    listOf(
                        ClientDetailsListItem.BankAccountModel(
                            number = "1234567891",
                            balance = "5000",
                            status = BankAccountStatus.OPEN,
                        )
                    )
                }

                else -> emptyList()
            }
            emit(State.Success(accountList))
        }

    //TODO replace with interactor
    private fun getLoansPage(pageNumber: Int): Flow<State<List<ClientDetailsListItem.LoanModel>>> =
        flow {
            emit(State.Loading)
            delay(1000)
            val loanList = when (pageNumber) {
                0 -> {
                    listOf(
                        ClientDetailsListItem.LoanModel(
                            number = "1234567890",
                            currentDebt = "5000",
                        ),
                        ClientDetailsListItem.LoanModel(
                            number = "0987654321",
                            currentDebt = "0",
                        ),
                        ClientDetailsListItem.LoanModel(
                            number = "135792468",
                            currentDebt = "10000",
                        ),
                        ClientDetailsListItem.LoanModel(
                            number = "2468135790",
                            currentDebt = "20000",
                        ),
                        ClientDetailsListItem.LoanModel(
                            number = "9876543210",
                            currentDebt = "30000",
                        ),
                    )
                }

                1 -> {
                    listOf(
                        ClientDetailsListItem.LoanModel(
                            number = "1234567891",
                            currentDebt = "30000",
                        ),
                    )
                }

                else -> emptyList()
            }
            emit(State.Success(loanList))
        }

    companion object {
        const val PAGE_SIZE = 5
    }
}