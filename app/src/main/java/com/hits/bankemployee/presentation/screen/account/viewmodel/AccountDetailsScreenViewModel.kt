package com.hits.bankemployee.presentation.screen.account.viewmodel

import com.hits.bankemployee.presentation.screen.account.event.AccountDetailsScreenEvent
import com.hits.bankemployee.presentation.screen.account.mapper.AccountDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.account.model.AccountDetailsListItem
import com.hits.bankemployee.presentation.screen.account.model.AccountDetailsPaginationState
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode
import com.hits.bankemployee.domain.interactor.BankAccountInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.domain.mergeWith
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationViewModel
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.back

private const val ACCOUNT_ID = "accountId"
private const val ACCOUNT_NUMBER = "accountNumber"
private const val ACCOUNT_BALANCE = "accountBalance"
private const val CURRENCY_CODE = "currencyCode"
private const val ACCOUNT_STATUS = "accountStatus"

@HiltViewModel(assistedFactory = AccountDetailsScreenViewModel.Factory::class)
class AccountDetailsScreenViewModel @AssistedInject constructor(
    @Assisted(ACCOUNT_ID) private val accountId: String,
    @Assisted(ACCOUNT_NUMBER) private val accountNumber: String?,
    @Assisted(ACCOUNT_BALANCE) private val accountBalance: String?,
    @Assisted(CURRENCY_CODE) private val currencyCode: CurrencyCode?,
    @Assisted(ACCOUNT_STATUS) private val accountStatusEntity: BankAccountStatusEntity?,
    private val bankAccountInteractor: BankAccountInteractor,
    private val mapper: AccountDetailsScreenModelMapper,
    private val navigationManager: NavigationManager,
) : PaginationViewModel<AccountDetailsListItem, AccountDetailsPaginationState>(
    BankUiState.Ready(AccountDetailsPaginationState.EMPTY)
) {

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onEvent(event: AccountDetailsScreenEvent) {
        when (event) {
            AccountDetailsScreenEvent.NavigateBack -> {
                navigationManager.back()
            }
        }
    }

    override fun getNextPageContents(pageNumber: Int): Flow<State<List<AccountDetailsListItem>>> {
        return if (pageNumber == 1) {
            if (accountNumber != null && accountBalance != null && currencyCode != null && accountStatusEntity != null) {
                val accountListItems = getAccountInfoItemsFromArgs(
                    accountId,
                    accountNumber,
                    accountBalance,
                    currencyCode,
                    accountStatusEntity,
                )
                loadOperationHistoryPage(pageNumber, accountListItems)
            } else {
                flow {
                    emit(State.Loading)
                    coroutineScope {
                        val accountListItemsRequest = async { tryLoadAccountInfoItems(accountId) }
                        val operationHistoryRequest = async { loadOperationHistoryPage(pageNumber).last() }
                        val accountListItemsResult = accountListItemsRequest.await()
                        val operationHistoryResult = operationHistoryRequest.await()
                        emit(accountListItemsResult.mergeWith(operationHistoryResult) { accountListItems, operationHistory ->
                            accountListItems + operationHistory
                        })
                    }
                }
            }
        } else {
            loadOperationHistoryPage(pageNumber)
        }
    }

    private suspend fun tryLoadAccountInfoItems(accountId: String): State<List<AccountDetailsListItem>> {
        return bankAccountInteractor.getAccountDetails(accountId).last().map { accountEntity ->
            val account = mapper.map(accountEntity)
            listOf(
                AccountDetailsListItem.AccountDetailsHeader,
                *account.toTypedArray(),
                AccountDetailsListItem.OperationHistoryHeader,
            )
        }
    }

    private fun getAccountInfoItemsFromArgs(
        accountId: String,
        accountNumber: String,
        accountBalance: String,
        currencyCode: CurrencyCode,
        accountStatusEntity: BankAccountStatusEntity
    ): List<AccountDetailsListItem> {
        val account = mapper.map(
            BankAccountEntity(
                accountId,
                accountNumber,
                accountBalance,
                currencyCode,
                accountStatusEntity,
            )
        )
        val accountListItems = listOf(
            AccountDetailsListItem.AccountDetailsHeader,
            *account.toTypedArray(),
            AccountDetailsListItem.OperationHistoryHeader,
        )
        return accountListItems
    }

    private fun loadOperationHistoryPage(
        pageNumber: Int,
        prependData: List<AccountDetailsListItem> = emptyList()
    ): Flow<State<List<AccountDetailsListItem>>> {
        return bankAccountInteractor.getAccountOperationHistory(
            accountId = accountId,
            pageInfo = PageInfo(pageSize = PAGE_SIZE, pageNumber = pageNumber),
        ).map { state ->
            state.map { list ->
                prependData + list.map { operation ->
                    mapper.map(operation)
                }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 5
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(ACCOUNT_ID) accountId: String,
            @Assisted(ACCOUNT_NUMBER) accountNumber: String?,
            @Assisted(ACCOUNT_BALANCE) accountBalance: String?,
            @Assisted(CURRENCY_CODE) currencyCode: CurrencyCode?,
            @Assisted(ACCOUNT_STATUS) accountStatusEntity: BankAccountStatusEntity?,
        ): AccountDetailsScreenViewModel
    }
}