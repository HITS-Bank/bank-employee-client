package com.hits.bankemployee.presentation.pagination

sealed interface PaginationEvent {

    object LoadNextPage : PaginationEvent

    object Reload : PaginationEvent
}