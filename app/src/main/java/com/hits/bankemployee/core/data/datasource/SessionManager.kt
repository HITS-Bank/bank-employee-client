package com.hits.bankemployee.core.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hits.bankemployee.core.data.model.TokenResponse
import com.hits.bankemployee.core.data.model.TokenType
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class SessionManager(context: Context) {

    private companion object {
        const val TOKEN_PREFERENCES_KEY = "token_preferences"
        const val ACCESS_TOKEN = "access_token"
        const val ACCESS_TOKEN_EXPIRES_AT = "access_token_expires_at"
        const val REFRESH_TOKEN = "refresh_token"
        const val REFRESH_TOKEN_EXPIRES_AT = "refresh_token_expires_at"
    }

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        TOKEN_PREFERENCES_KEY,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun fetchToken(tokenType: TokenType): String? {
        return when (tokenType) {
            TokenType.ACCESS -> sharedPreferences.getString(ACCESS_TOKEN, null)
            TokenType.REFRESH -> sharedPreferences.getString(REFRESH_TOKEN, null)
        }
    }

    fun saveTokens(tokenResponse: TokenResponse) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN, tokenResponse.accessToken)
            .putString(ACCESS_TOKEN_EXPIRES_AT, tokenResponse.accessTokenExpiresAt)
            .putString(REFRESH_TOKEN, tokenResponse.refreshToken)
            .putString(REFRESH_TOKEN_EXPIRES_AT, tokenResponse.refreshTokenExpiresAt)
            .apply()
    }

    fun isTokenExpired(tokenType: TokenType): Boolean {
        return when (tokenType) {
            TokenType.ACCESS -> {
                val expiresAt = sharedPreferences.getString(ACCESS_TOKEN_EXPIRES_AT, "").parseUtcDateTime()
                expiresAt?.let { it < LocalDateTime.now() } ?: true
            }

            TokenType.REFRESH -> {
                val expiresAt = sharedPreferences.getString(REFRESH_TOKEN_EXPIRES_AT, "").parseUtcDateTime()
                expiresAt?.let { it < LocalDateTime.now() } ?: true
            }
        }
    }

    fun hasToken(): Boolean = sharedPreferences.getString(ACCESS_TOKEN, null) != null

    private fun String?.parseUtcDateTime(): LocalDateTime? {
        return try {
            LocalDateTime.parse(this)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}