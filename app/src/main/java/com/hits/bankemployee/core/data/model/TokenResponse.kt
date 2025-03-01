package com.hits.bankemployee.core.data.model

data class TokenResponse(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String,
)