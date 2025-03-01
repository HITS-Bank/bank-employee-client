package com.hits.bankemployee.core.presentation.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class PaginationViewModel<T>(initState: BankUiState<PaginationStateHolder<T>>) : ViewModel() {

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
            paginationEvents.collectLatest { event ->
                processPaginationEvent(event)
            }
        }
    }

    private suspend fun processPaginationEvent(event: PaginationEvent) {
        when (event) {
            is PaginationEvent.LoadNextPage -> {
                _state.updateIfSuccess(onUpdated = { loadPage(true) }) { state ->
                    state.copyWith(paginationState = PaginationState.Loading)
                }
            }

            is PaginationEvent.Reload -> {
                _state.updateIfSuccess(onUpdated = { loadPage(false) }) { state ->
                    state.resetPagination().copyWith(paginationState = PaginationState.Loading)
                }
            }
        }
    }

    private suspend fun loadPage(incrementPageNumber: Boolean) {
        val stateValue = state.getIfSuccess() ?: return
        val nextPageNumber = stateValue.pageNumber + if (incrementPageNumber) 1 else 0
        getNextPageContents(nextPageNumber).collect { state ->
            when (state) {
                is State.Error -> _state.updateIfSuccess { oldState ->
                    oldState.copyWith(paginationState = PaginationState.Error)
                }
                State.Loading -> Unit
                is State.Success<List<T>> -> _state.updateIfSuccess { oldState ->
                    oldState.copyWith(
                        paginationState =
                            if (state.data.size < oldState.pageSize) PaginationState.EndReached
                            else PaginationState.Idle,
                        data = oldState.data + state.data,
                        pageNumber = nextPageNumber,
                    )
                }
            }
        }
    }

    protected abstract suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<T>>>
}