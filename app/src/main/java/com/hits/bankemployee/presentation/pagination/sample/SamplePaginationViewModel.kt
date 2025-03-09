package com.hits.bankemployee.presentation.pagination.sample

import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.presentation.common.BankUiState
import com.hits.bankemployee.presentation.pagination.PaginationEvent
import com.hits.bankemployee.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SamplePaginationViewModel : PaginationViewModel<Int, SamplePaginationState>(BankUiState.Ready(SamplePaginationState())) {

    private var nextPageCallCount = -1

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    override fun getNextPageContents(pageNumber: Int): Flow<State<List<Int>>> = flow {
        emit(State.Loading)
        delay(1000)
        nextPageCallCount++
        if (nextPageCallCount % 4 == 0) {
            emit(State.Error())
            return@flow
        }
        emit(
            State.Success(
                listOf(
                    3 * (pageNumber) + 1,
                    3 * (pageNumber) + 2,
                    3 * (pageNumber) + 3,
                )
            )
        )
    }
}