package com.hits.bankemployee.core.presentation.pagination.sample

import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.PaginationStateHolder

data class SamplePaginationState(
    override val paginationState: PaginationState = PaginationState.Idle,
    override val data: List<Int> = emptyList(),
    override val pageNumber: Int = 0,
    override val pageSize: Int = 3,
) : PaginationStateHolder<Int> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<Int>,
        pageNumber: Int
    ): PaginationStateHolder<Int> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<Int> {
        return copy(data = emptyList(), pageNumber = 0)
    }
}
