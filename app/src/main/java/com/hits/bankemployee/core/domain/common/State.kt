package com.hits.bankemployee.core.domain.common

object Completable : Any()

sealed interface State<out T : Any> {

    data object Loading : State<Nothing>

    data class Success<T : Any>(val data: T) : State<T>

    data class Error(val throwable: Throwable? = null) : State<Nothing>
}

inline fun <T : Any, R : Any> State<T>.map(mapper: (T) -> (R)): State<R> {
    return when (this) {
        is State.Loading -> this
        is State.Error -> this
        is State.Success -> State.Success(mapper(data))
    }
}