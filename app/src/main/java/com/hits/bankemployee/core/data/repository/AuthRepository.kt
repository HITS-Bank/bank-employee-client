package com.hits.bankemployee.core.data.repository

import com.hits.bankemployee.core.data.api.AuthApi
import com.hits.bankemployee.core.data.common.apiCall
import com.hits.bankemployee.core.data.common.toResult
import com.hits.bankemployee.core.data.datasource.SessionManager
import com.hits.bankemployee.core.data.mapper.AuthMapper
import com.hits.bankemployee.core.data.model.RefreshRequest
import com.hits.bankemployee.core.data.model.TokenType
import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.common.toCompletableResult
import com.hits.bankemployee.core.domain.entity.LoginRequestEntity
import com.hits.bankemployee.core.domain.repository.IAuthRepository
import com.hits.bankemployee.core.domain.common.Result
import kotlinx.coroutines.Dispatchers

class AuthRepository(
    private val authApi: AuthApi,
    private val mapper: AuthMapper,
    private val sessionManager: SessionManager,
) : IAuthRepository {

    override suspend fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            authApi.login(channel, mapper.map(request))
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveTokens(result.data)
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun refresh(): Result<Completable> {
        val accessToken = sessionManager.fetchToken(TokenType.ACCESS)
        val refreshToken = sessionManager.fetchToken(TokenType.REFRESH)
        if (accessToken == null || refreshToken == null) {
            return Result.Error(Exception("could not retrieve tokens"))
        }

        return apiCall(Dispatchers.IO) {
            authApi.refresh(
                expiredToken = "Bearer $accessToken",
                request = RefreshRequest(refreshToken),
            )
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveTokens(result.data)
                    }
                }
                .toCompletableResult()
        }
    }

    override fun saveIsUserBlocked(isUserBlocked: Boolean) {
        sessionManager.saveIsUserBlocked(isUserBlocked)
    }

    override fun getIsUserBlocked(): Result<Boolean> {
        return Result.Success(sessionManager.isUserBlocked())
    }
}