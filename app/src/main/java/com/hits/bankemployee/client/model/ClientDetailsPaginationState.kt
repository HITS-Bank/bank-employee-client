package com.hits.bankemployee.client.model

import com.hits.bankemployee.client.viewmodel.ClientDetailsScreenViewModel
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.PaginationStateHolder

data class ClientDetailsPaginationState(
    override val paginationState: PaginationState,
    override val data: List<ClientDetailsListItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val client: ClientModel,
) : PaginationStateHolder<ClientDetailsListItem> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<ClientDetailsListItem>,
        pageNumber: Int
    ): PaginationStateHolder<ClientDetailsListItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<ClientDetailsListItem> {
        return copy(data = emptyList(), pageNumber = 0)
    }

    companion object {
        fun empty(client: ClientModel) = ClientDetailsPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 0,
            pageSize = ClientDetailsScreenViewModel.PAGE_SIZE,
            client = client,
        )
    }
}
