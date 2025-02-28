package com.hits.bankemployee.presentation.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.common.runSuspendCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class PaginationViewModel<T>(initState: PaginationStateHolder<T>) : ViewModel() {

    protected val _state = MutableStateFlow(initState)
    val state = _state.asStateFlow()

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onPaginationEvent(event: PaginationEvent) {
        when (event) {
            is PaginationEvent.LoadNextPage -> {
                if (state.value.paginationState == PaginationState.Loading) return
                _state.value = state.value.copyWith(paginationState = PaginationState.Loading)
                loadNextPage()
            }

            is PaginationEvent.Reload -> {
                if (state.value.paginationState == PaginationState.Loading) return
                _state.value = state.value.resetPagination().copyWith(paginationState = PaginationState.Loading)
                loadNextPage()
            }
        }
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            val nextPageNumber = state.value.pageNumber + 1
            runSuspendCatching {
                getNextPageContents(nextPageNumber)
            }.onSuccess { newContents ->
                _state.value = state.value.copyWith(
                    paginationState = if (newContents.size < state.value.pageSize) PaginationState.EndReached else PaginationState.Idle,
                    data = state.value.data + newContents,
                    pageNumber = nextPageNumber,
                )
            }.onFailure {
                _state.value = state.value.copyWith(paginationState = PaginationState.Error)
            }
        }
    }

    protected abstract suspend fun getNextPageContents(pageNumber: Int): List<T>
}