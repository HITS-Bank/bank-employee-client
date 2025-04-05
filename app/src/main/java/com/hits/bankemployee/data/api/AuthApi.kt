package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val AUTH_TOKEN_PATH = "realms/bank/protocol/openid-connect/token"

interface AuthApi {

    @FormUrlEncoded
    @POST(AUTH_TOKEN_PATH)
    suspend fun exchangeAuthCodeForToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST(AUTH_TOKEN_PATH)
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
    ): Response<TokenResponse>
}