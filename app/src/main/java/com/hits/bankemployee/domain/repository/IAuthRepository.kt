package com.hits.bankemployee.domain.repository

import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.common.Completable
import com.hits.bankemployee.domain.entity.LoginRequestEntity

interface IAuthRepository {

    suspend fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Result<Completable>

    suspend fun refresh(): Result<Completable>

    fun saveIsUserBlocked(isUserBlocked: Boolean)

    fun getIsUserBlocked(): Result<Boolean>
}