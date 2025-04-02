package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.AuthApi
import com.hits.bankemployee.data.datasource.SessionManager
import com.hits.bankemployee.data.mapper.AuthMapper
import com.hits.bankemployee.data.model.RefreshRequest
import com.hits.bankemployee.data.model.TokenType
import com.hits.bankemployee.domain.entity.LoginRequestEntity
import com.hits.bankemployee.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.apiCall
import ru.hitsbank.bank_common.data.toResult
import ru.hitsbank.bank_common.domain.Completable
import javax.inject.Inject
import javax.inject.Singleton
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.toCompletableResult

@Singleton
class AuthRepository @Inject constructor(
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