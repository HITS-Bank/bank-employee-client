package com.hits.bankemployee.domain.repository

import ru.hitsbank.bank_common.domain.Result
import com.hits.bankemployee.domain.entity.LoginRequestEntity
import ru.hitsbank.bank_common.domain.Completable

interface IAuthRepository {

    suspend fun exchangeAuthCodeForToken(code: String): Result<Completable>

    suspend fun refresh(): Result<Completable>

    fun saveIsUserBlocked(isUserBlocked: Boolean)

    fun getIsUserBlocked(): Result<Boolean>
}