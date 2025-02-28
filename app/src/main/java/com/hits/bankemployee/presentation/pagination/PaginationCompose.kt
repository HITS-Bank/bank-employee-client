package com.hits.bankemployee.presentation.pagination

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("ComposableNaming")
@Composable
fun PaginationViewModel<*>.listenToListStateAndLoadNewPages(listState: LazyListState, loadThreshold: Int = 2) {
    val state by state.collectAsStateWithLifecycle()
    val shouldLoadNextPage by remember(state) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val offsetThresholdReached =
                lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - loadThreshold
            offsetThresholdReached && state.paginationState == PaginationState.Idle
        }
    }
    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onPaginationEvent(PaginationEvent.LoadNextPage)
        }
    }
}