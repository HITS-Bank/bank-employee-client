package com.hits.bankemployee.core.presentation.pagination.sample

import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SamplePaginationViewModel : PaginationViewModel<String>(BankUiState.Ready(SamplePaginationState())) {

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<String>>> = flow {
        emit(State.Loading)
        delay(1000)
        emit(State.Success(listOf("Item ${3*(pageNumber) + 1}", "Item ${3*(pageNumber) + 2}", "Item ${3*(pageNumber) + 3}")))
    }
}