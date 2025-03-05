package com.hits.bankemployee.core.domain.common

sealed interface Result<out T> {

    data class Success<T : Any>(val data: T) : Result<T>

    data class Error(val throwable: Throwable? = null) : Result<Nothing>

}

fun <T> Result<T>.toCompletableResult() = when (this) {
    is Result.Error -> this
    is Result.Success -> Result.Success(Completable)
}

fun <T : Any> Result<T>.toState() = when (this) {
    is Result.Error -> State.Error(throwable)
    is Result.Success -> State.Success(data)
}

fun <T, R : Any> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Error -> this
    is Result.Success -> Result.Success(mapper(this.data))
}