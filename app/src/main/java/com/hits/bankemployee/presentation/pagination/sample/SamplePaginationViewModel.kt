package com.hits.bankemployee.presentation.pagination.sample

import com.hits.bankemployee.presentation.pagination.PaginationViewModel
import kotlinx.coroutines.delay

class SamplePaginationViewModel : PaginationViewModel<String>(SamplePaginationState()) {
    override suspend fun getNextPageContents(pageNumber: Int): List<String> {
        delay(2000)
        return listOf("Item ${3*(pageNumber-1) + 1}", "Item ${3*(pageNumber-1) + 2}", "Item ${3*(pageNumber-1) + 3}")
    }
}