package com.hits.bankemployee.presentation.pagination

interface PaginationStateHolder<T> {
    val paginationState: PaginationState
    val data: List<T>
    val pageNumber: Int
    val pageSize: Int

    fun copyWith(
        paginationState: PaginationState = this.paginationState,
        data: List<T> = this.data,
        pageNumber: Int = this.pageNumber,
    ): PaginationStateHolder<T>

    fun resetPagination(): PaginationStateHolder<T>
}

val PaginationStateHolder<*>.reloadState: PaginationReloadState
    get() = when {
        paginationState == PaginationState.Loading && data.isEmpty() -> PaginationReloadState.Reloading
        paginationState == PaginationState.Error && data.isEmpty() -> PaginationReloadState.Error
        else -> PaginationReloadState.Idle
    }