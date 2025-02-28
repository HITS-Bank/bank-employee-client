package com.hits.bankemployee.presentation.pagination.sample

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.presentation.pagination.PaginationState
import com.hits.bankemployee.presentation.pagination.listenToListStateAndLoadNewPages

@Composable
fun SamplePaginationScreen(viewModel: SamplePaginationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    viewModel.listenToListStateAndLoadNewPages(listState)

    LazyColumn(state = listState) {
        items(state.data) { item ->
            Text(text = item)
        }

        when(state.paginationState) {
            PaginationState.Loading -> item {
                CircularProgressIndicator()
            }
            PaginationState.Idle, PaginationState.Error, PaginationState.EndReached -> Unit
        }
    }
}