package com.hits.bankemployee.core.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T: CharSequence> Flow<T>.dropFirstBlank(): Flow<T> {
    return flow {
        var isFirstElementCollected = false
        collect { value ->
            if (!isFirstElementCollected) {
                if (value.isNotBlank()) {
                    emit(value)
                }
                isFirstElementCollected = true
            } else {
                emit(value)
            }
        }
    }
}
