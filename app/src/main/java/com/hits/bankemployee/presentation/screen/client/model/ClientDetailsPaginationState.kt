package com.hits.bankemployee.presentation.screen.client.model

import androidx.annotation.DrawableRes
import com.hits.bankemployee.R
import com.hits.bankemployee.presentation.screen.client.viewmodel.ClientDetailsScreenViewModel
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.PaginationStateHolder

data class ClientDetailsPaginationState(
    override val paginationState: PaginationState,
    override val data: List<ClientDetailsListItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val client: ClientModel,
    val isDialogVisible: Boolean,
    val isPerformingAction: Boolean,
) : PaginationStateHolder<ClientDetailsListItem> {

    val fabText
        get() = if (client.isBlocked) "Разблокировать" else "Заблокировать"
    val fabIconResId
        @DrawableRes get() = if (client.isBlocked) R.drawable.ic_unblock else R.drawable.ic_block

    override fun copyWith(
        paginationState: PaginationState,
        data: List<ClientDetailsListItem>,
        pageNumber: Int
    ): PaginationStateHolder<ClientDetailsListItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<ClientDetailsListItem> {
        return copy(data = emptyList(), pageNumber = 1)
    }

    companion object {
        fun empty(client: ClientModel) = ClientDetailsPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = ClientDetailsScreenViewModel.PAGE_SIZE,
            client = client,
            isDialogVisible = false,
            isPerformingAction = false,
        )
    }
}
