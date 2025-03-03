package com.hits.bankemployee.core.presentation.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.core.presentation.common.getIfSuccess

@Composable
fun rememberPaginationListState(
    viewModel: PaginationViewModel<*, *>,
    loadThreshold: Int = 2,
): LazyListState {
    val listState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val shouldLoadNextPage by remember(state) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val offsetThresholdReached =
                lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - loadThreshold

            offsetThresholdReached && state.getIfSuccess()?.paginationState == PaginationState.Idle
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            viewModel.onPaginationEvent(PaginationEvent.LoadNextPage)
        }
    }

    return listState
}