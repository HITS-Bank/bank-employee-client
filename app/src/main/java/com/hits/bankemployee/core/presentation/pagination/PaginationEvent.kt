package com.hits.bankemployee.core.presentation.pagination

sealed interface PaginationEvent {

    object LoadNextPage : PaginationEvent

    object Reload : PaginationEvent
}