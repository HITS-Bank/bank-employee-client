package com.hits.bankemployee.presentation.screen.account.model

import androidx.compose.ui.graphics.Color
import com.hits.bankemployee.presentation.screen.account.viewmodel.AccountDetailsScreenViewModel
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.PaginationStateHolder

data class AccountDetailsPaginationState(
    override val paginationState: PaginationState,
    override val data: List<AccountDetailsListItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
) : PaginationStateHolder<AccountDetailsListItem> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<AccountDetailsListItem>,
        pageNumber: Int
    ): PaginationStateHolder<AccountDetailsListItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<AccountDetailsListItem> {
        return copy(data = emptyList(), pageNumber = 1)
    }

    companion object {
        val EMPTY = AccountDetailsPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = AccountDetailsScreenViewModel.PAGE_SIZE,
        )
    }
}

sealed interface AccountDetailsListItem {

    data object AccountDetailsHeader : AccountDetailsListItem

    data object OperationHistoryHeader : AccountDetailsListItem

    data class AccountDetailsProperty(
        val name: String,
        val value: String,
    ) : AccountDetailsListItem

    data class OperationHistoryItem(
        val id: String,
        val date: String,
        val amount: String,
        val operationTitle: String,
        val amountColor: Color,
        val iconColor: Color,
        val iconBackground: Color,
    ) : AccountDetailsListItem

}