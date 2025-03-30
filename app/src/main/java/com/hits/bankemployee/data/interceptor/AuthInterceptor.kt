package com.hits.bankemployee.data.interceptor

import com.hits.bankemployee.data.datasource.SessionManager
import com.hits.bankemployee.data.model.TokenType
import com.hits.bankemployee.domain.repository.IAuthRepository
import com.hits.bankemployee.domain.common.Result
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
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
                    } else {
                        null
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