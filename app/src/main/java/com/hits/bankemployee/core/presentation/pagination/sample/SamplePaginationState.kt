package com.hits.bankemployee.core.presentation.pagination.sample

import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.PaginationStateHolder

data class SamplePaginationState(
    override val paginationState: PaginationState = PaginationState.Idle,
    override val data: List<String> = emptyList(),
    override val pageNumber: Int = 0,
    override val pageSize: Int = 3,
) : PaginationStateHolder<String> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<String>,
        pageNumber: Int
    ): PaginationStateHolder<String> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<String> {
        return copy(data = emptyList(), pageNumber = 0)
    }
}
