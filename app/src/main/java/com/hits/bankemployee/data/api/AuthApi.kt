package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.LoginRequest
import com.hits.bankemployee.data.model.RefreshRequest
import com.hits.bankemployee.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Query("channel") channel: String,
        @Body request: LoginRequest,
    ): Response<TokenResponse>

    @POST("auth/refresh")
    suspend fun refresh(
        @Header("Authorization") expiredToken: String,
        @Body request: RefreshRequest,
    ): Response<TokenResponse>
}