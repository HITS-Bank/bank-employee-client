package com.hits.bankemployee.core.data.interceptor

import com.hits.bankemployee.core.data.datasource.SessionManager
import com.hits.bankemployee.core.data.model.TokenType
import com.hits.bankemployee.core.domain.repository.IAuthRepository
import com.hits.bankemployee.core.domain.common.Result
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepository: IAuthRepository,
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = sessionManager.fetchToken(TokenType.ACCESS)
        val isAccessTokenExpired = sessionManager.isTokenExpired(TokenType.ACCESS)

        if (accessToken != null && isAccessTokenExpired) {
            val refreshToken = sessionManager.fetchToken(TokenType.REFRESH)
            val isRefreshTokenExpired = sessionManager.isTokenExpired(TokenType.REFRESH)

            if (isRefreshTokenExpired) {
                return chain.proceed(originalRequest)
            }

            val newAccessToken = runBlocking {
                refreshToken?.let {
                    val refreshResult = authRepository.refresh()
                    if (refreshResult !is Result.Error) {
                        sessionManager.fetchToken(TokenType.ACCESS)
                    }
                }
            }

            if (newAccessToken != null) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()

                return chain.proceed(newRequest)
            }
        }

        val authorizedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }
}