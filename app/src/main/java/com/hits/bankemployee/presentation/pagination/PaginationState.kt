package com.hits.bankemployee.presentation.pagination

sealed interface PaginationState {

    object Idle : PaginationState

    object Loading : PaginationState

    object Error : PaginationState

    object EndReached : PaginationState

}