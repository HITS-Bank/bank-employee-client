package com.hits.bankemployee.core.presentation.pagination

sealed interface PaginationState {

    object Idle : PaginationState

    object Loading : PaginationState

    object Error : PaginationState

    object EndReached : PaginationState

}

sealed interface PaginationReloadState {

    object Idle : PaginationReloadState

    object Reloading : PaginationReloadState

    object Error : PaginationReloadState

}