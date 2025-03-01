package com.hits.bankemployee.core.presentation.pagination.sample

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationReloadState
import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.reloadState
import com.hits.bankemployee.core.presentation.pagination.rememberPaginationListState
import com.hits.bankemployee.core.presentation.compose.component.FullScreenProgressIndicator

@Composable
fun SamplePaginationScreen(viewModel: SamplePaginationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberPaginationListState(viewModel)

    Crossfade(targetState = state.reloadState) { reloadState ->
        when (reloadState) {
            PaginationReloadState.Idle -> {
                LazyColumn(state = listState) {
                    items(state.data) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = item)
                        }
                    }

                    when (state.paginationState) {
                        PaginationState.Loading -> item {
                            FullScreenProgressIndicator()
                        }

                        PaginationState.Error -> item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Error", modifier = Modifier.weight(1f))
                                Button(
                                    onClick = {
                                        viewModel.onPaginationEvent(PaginationEvent.LoadNextPage)
                                    },
                                ) {
                                    Text(text = "Retry")
                                }
                            }
                        }

                        PaginationState.Idle, PaginationState.EndReached -> Unit
                    }
                }
            }

            PaginationReloadState.Reloading -> FullScreenProgressIndicator()
            PaginationReloadState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Error")
                    Button(
                        onClick = {
                            viewModel.onPaginationEvent(PaginationEvent.Reload)
                        },
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}