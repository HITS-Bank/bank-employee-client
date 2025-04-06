package com.hits.bankemployee.presentation.screen.client.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.interactor.BankAccountInteractor
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.domain.interactor.UserInteractor
import com.hits.bankemployee.presentation.navigation.BankAccountDetails
import com.hits.bankemployee.presentation.navigation.LoanPayments
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEffect
import com.hits.bankemployee.presentation.screen.client.event.ClientDetailsScreenEvent
import com.hits.bankemployee.presentation.screen.client.mapper.ClientDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.client.model.ClientDetailsListItem
import com.hits.bankemployee.presentation.screen.client.model.ClientDetailsPaginationState
import com.hits.bankemployee.presentation.screen.client.model.ClientModel
import com.hits.bankemployee.presentation.screen.client.model.toEntity
import com.hits.bankemployee.presentation.screen.users.model.toUserRole
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.updateIfSuccess
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationViewModel

@HiltViewModel(assistedFactory = ClientDetailsScreenViewModel.Factory::class)
class ClientDetailsScreenViewModel @AssistedInject constructor(
    @Assisted private val client: ClientModel,
    private val userInteractor: UserInteractor,
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
                    userInteractor.unbanUser(clientId)
                } else {
                    userInteractor.banUser(clientId)
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
                    LoanPayments.destinationWithArgs(event.id)
                ) {
                    onPaginationEvent(PaginationEvent.Reload)
                }
            }

            is ClientDetailsScreenEvent.BankAccountClicked -> {
                navigationManager.forwardWithCallbackResult(
                    BankAccountDetails.withArgs(
                        bankAccountId = event.id,
                        bankAccountNumber = event.number,
                        bankAccountBalance = event.balance,
                        currencyCode = event.currencyCode.name,
                        bankAccountStatus = event.status.toEntity().name,
                    )
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
            if (pageNumber == 1) {
                bankAccountListLastPageNumber = null
            }
            val bankAccountPageNumber = bankAccountListLastPageNumber
            if (bankAccountPageNumber == null) {
                loadAccountsPage(pageNumber)
            } else {
                val loans = loanInteractor.getLoans(
                    client.id,
                    pageInfo = PageInfo(pageSize = PAGE_SIZE, pageNumber - bankAccountPageNumber + 1),
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
            val roles = _state.getIfSuccess()?.client?.roles
            val roleList = roles?.map { it.toUserRole().title }
            if (pageNumber == 1) {
                if (roleList != null) {
                    accountList.add(ClientDetailsListItem.RolesModel(roleList))
                }
                val loanRating = loanInteractor.getLoanRating(client.id).last()
                if (loanRating is State.Success) {
                    accountList.add(ClientDetailsListItem.LoanRatingModel(loanRating.data.toString()))
                }
                if (_state.getIfSuccess()?.client?.isBlocked == true) {
                    accountList.add(ClientDetailsListItem.IsBlockedModel)
                }
                if (accountList.isNotEmpty()) {
                    accountList.add(0, ClientDetailsListItem.UserInfoHeader)
                }
                accountList.add(ClientDetailsListItem.AccountsHeader)
            }
            if (roles == null || !roles.contains(RoleType.CLIENT)) {
                return
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
            pageInfo = PageInfo(pageSize = PAGE_SIZE, 1),
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

    @AssistedFactory
    interface Factory {
        fun create(client: ClientModel): ClientDetailsScreenViewModel
    }
}