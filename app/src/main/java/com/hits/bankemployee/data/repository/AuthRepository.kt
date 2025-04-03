package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.AuthApi
import com.hits.bankemployee.data.datasource.SessionManager
import com.hits.bankemployee.data.mapper.AuthMapper
import com.hits.bankemployee.data.model.TokenType
import com.hits.bankemployee.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.Constants.AUTH_CLIENT_ID
import ru.hitsbank.bank_common.Constants.AUTH_REDIRECT_URI
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toResult
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.toCompletableResult
import javax.inject.Inject
import javax.inject.Singleton

private const val AUTH_CODE_GRANT_TYPE = "authorization_code"
private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val mapper: AuthMapper,
    private val sessionManager: SessionManager,
) : IAuthRepository {

    override suspend fun exchangeAuthCodeForToken(code: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            authApi.exchangeAuthCodeForToken(
                clientId = AUTH_CLIENT_ID,
                grantType = AUTH_CODE_GRANT_TYPE,
                code = code,
                redirectUri = "hitsbankapp://employee_authorized",
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

    override suspend fun refresh(): Result<Completable> {
        val refreshToken = sessionManager.fetchToken(TokenType.REFRESH)
            ?: return Result.Error(Exception("could not retrieve refresh token"))

        return apiCall(Dispatchers.IO) {
            authApi.refreshToken(
                clientId = AUTH_CLIENT_ID,
                grantType = REFRESH_TOKEN_GRANT_TYPE,
                refreshToken = refreshToken,
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