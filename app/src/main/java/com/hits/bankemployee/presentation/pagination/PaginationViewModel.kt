package com.hits.bankemployee.presentation.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.common.runSuspendCatching
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class PaginationViewModel<T>(initState: PaginationStateHolder<T>) : ViewModel() {

    protected val _state = MutableStateFlow(initState)
    val state = _state.asStateFlow()

    private val paginationEvents = MutableSharedFlow<PaginationEvent>()

    init {
        subscribeToPaginationEvents()
    }

    fun onPaginationEvent(event: PaginationEvent) {
        viewModelScope.launch {
            paginationEvents.emit(event)
        }
    }

    private fun subscribeToPaginationEvents() {
        viewModelScope.launch {
            paginationEvents.onSubscription {
                emit(PaginationEvent.Reload)
            }.collectLatest { event ->
                processPaginationEvent(event)
            }
        }
    }

    private suspend fun processPaginationEvent(event: PaginationEvent) {
        when (event) {
            is PaginationEvent.LoadNextPage -> {
                _state.update { state ->
                    state.copyWith(paginationState = PaginationState.Loading)
                }
                loadPage(true)
            }

            is PaginationEvent.Reload -> {
                _state.update { state ->
                    state.resetPagination().copyWith(paginationState = PaginationState.Loading)
                }
                loadPage(false)
            }
        }
    }

    private suspend fun loadPage(incrementPageNumber: Boolean) {
        val nextPageNumber = state.value.pageNumber + if (incrementPageNumber) 1 else 0
        runSuspendCatching {
            getNextPageContents(nextPageNumber)
        }.onSuccess { newContents ->
            _state.update { state ->
                state.copyWith(
                    paginationState =
                        if (newContents.size < state.pageSize) PaginationState.EndReached
                        else PaginationState.Idle,
                    data = state.data + newContents,
                    pageNumber = nextPageNumber,
                )
            }
        }.onFailure {
            _state.update { state ->
                state.copyWith(paginationState = PaginationState.Error)
            }
        }
    }

    protected abstract suspend fun getNextPageContents(pageNumber: Int): List<T>
}