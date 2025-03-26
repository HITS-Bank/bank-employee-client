package com.hits.bankemployee.presentation.screen.account.viewmodel

import com.hits.bankemployee.presentation.screen.account.event.AccountDetailsScreenEvent
import com.hits.bankemployee.presentation.screen.account.mapper.AccountDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.account.model.AccountDetailsListItem
import com.hits.bankemployee.presentation.screen.account.model.AccountDetailsPaginationState
import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.common.map
import com.hits.bankemployee.domain.common.mergeWith
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode
import com.hits.bankemployee.domain.interactor.BankAccountInteractor
import com.hits.bankemployee.presentation.common.BankUiState
import com.hits.bankemployee.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.presentation.navigation.base.back
import com.hits.bankemployee.presentation.pagination.PaginationEvent
import com.hits.bankemployee.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class AccountDetailsScreenViewModel(
    private val accountId: String,
    private val accountNumber: String?,
    private val accountBalance: String?,
    private val currencyCode: CurrencyCode?,
    private val accountStatusEntity: BankAccountStatusEntity?,
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
}