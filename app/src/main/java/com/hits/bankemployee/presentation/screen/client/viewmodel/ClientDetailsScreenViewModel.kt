package com.hits.bankemployee.presentation.screen.client.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEffect
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEvent
import com.hits.bankemployee.presentation.screen.client.mapper.ClientDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.client.model.ClientDetailsListItem
import com.hits.bankemployee.presentation.screen.client.model.ClientDetailsPaginationState
import com.hits.bankemployee.presentation.screen.client.model.ClientModel
import com.hits.bankemployee.presentation.screen.client.model.toEntity
import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.common.map
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.interactor.BankAccountInteractor
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.domain.interactor.ProfileInteractor
import com.hits.bankemployee.presentation.common.BankUiState
import com.hits.bankemployee.presentation.common.getIfSuccess
import com.hits.bankemployee.presentation.common.updateIfSuccess
import com.hits.bankemployee.presentation.navigation.BankAccountDetails
import com.hits.bankemployee.presentation.navigation.LoanDetails
import com.hits.bankemployee.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.presentation.navigation.base.back
import com.hits.bankemployee.presentation.navigation.base.forwardWithCallbackResult
import com.hits.bankemployee.presentation.pagination.PaginationEvent
import com.hits.bankemployee.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ClientDetailsScreenViewModel(
    private val client: ClientModel,
    private val profileInteractor: ProfileInteractor,
    private val bankAccountInteractor: BankAccountInteractor,
    private val loanInteractor: LoanInteractor,
    private val navigationManager: NavigationManager,
    private val mapper: ClientDetailsScreenModelMapper,
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
                    BankAccountDetails.withArgs(event.number, event.balance, event.status.toEntity().name)
                ) {
                    onPaginationEvent(PaginationEvent.Reload)
                }
            }
        }
    }

    //TODO при возможности переписать (оно работает, но выглядит не очень красиво и читаемо)
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
                val loans = loanInteractor.getLoans(
                    client.id,
                    pageInfo = PageInfo(pageSize = PAGE_SIZE, pageNumber - bankAccountPageNumber),
                )
                emit(loans.last().map { list -> list.map { mapper.map(it) } })
            }
        }

    private suspend fun FlowCollector<State<List<ClientDetailsListItem>>>.loadAccountsPage(
        pageNumber: Int
    ) {
        val accounts = bankAccountInteractor.getAccountList(
            client.id,
            PageInfo(pageSize = PAGE_SIZE, pageNumber)
        )
        val pageResult = accounts.last().map { list -> list.map { mapper.map(it) } }
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
        val loans = loanInteractor.getLoans(
            client.id,
            pageInfo = PageInfo(pageSize = PAGE_SIZE, 0),
        )
        val loanPageResult = loans.last().map { list -> list.map { mapper.map(it) } }
        if (loanPageResult is State.Success) {
            accountList.add(ClientDetailsListItem.LoansHeader)
            accountList.addAll(loanPageResult.data)
            return true
        } else {
            emit(loanPageResult)
            return false
        }
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}