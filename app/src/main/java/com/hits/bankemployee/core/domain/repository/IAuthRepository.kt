package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.entity.LoginRequestEntity

interface IAuthRepository {

    suspend fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Result<Completable>

    suspend fun refresh(): Result<Completable>
}