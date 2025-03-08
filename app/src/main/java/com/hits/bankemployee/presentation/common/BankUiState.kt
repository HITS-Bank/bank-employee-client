package com.hits.bankemployee.presentation.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

sealed interface BankUiState<out T> {

    data object Loading : BankUiState<Nothing>

    data class Error(val error: Throwable? = null) : BankUiState<Nothing>

    data class Ready<T>(val model: T) : BankUiState<T>
}

fun <T> StateFlow<BankUiState<T>>.getIfSuccess(): T? {
    return (this.value as? BankUiState.Ready)?.model
}

fun <T> BankUiState<T>.getIfSuccess(): T? {
    return (this as? BankUiState.Ready)?.model
}

fun <T> MutableStateFlow<BankUiState<T>>.updateIfSuccess(reducer: (T) -> T) {
    val readyState = this.value as? BankUiState.Ready
    if (readyState != null) {
        this.update { readyState.copy(reducer(readyState.model)) }
    }
}

inline fun <T> MutableStateFlow<BankUiState<T>>.updateIfSuccess(onUpdated: () -> Unit, reducer: (T) -> T) {
    val readyState = this.value as? BankUiState.Ready
    if (readyState != null) {
        this.update { readyState.copy(reducer(readyState.model)) }
        onUpdated()
    }
}