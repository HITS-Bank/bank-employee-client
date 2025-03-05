package com.hits.bankemployee.core.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hits.bankemployee.core.data.model.TokenResponse
import com.hits.bankemployee.core.data.model.TokenType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class SessionManager(context: Context) {

    private companion object {
        const val TOKEN_PREFERENCES_KEY = "token_preferences"
        const val ACCESS_TOKEN = "access_token"
        const val ACCESS_TOKEN_EXPIRES_AT = "access_token_expires_at"
        const val REFRESH_TOKEN = "refresh_token"
        const val REFRESH_TOKEN_EXPIRES_AT = "refresh_token_expires_at"
        const val IS_USER_BLOCKED = "is_user_blocked"
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

    fun saveIsUserBlocked(isBlocked: Boolean) {
        sharedPreferences.edit().putBoolean(IS_USER_BLOCKED, isBlocked).apply()
    }

    fun isUserBlocked(): Boolean = sharedPreferences.getBoolean(IS_USER_BLOCKED, false)

    fun hasToken(): Boolean = sharedPreferences.getString(ACCESS_TOKEN, null) != null

    private fun String?.parseUtcDateTime(): LocalDateTime? {
        return try {
            Instant.parse(this).atZone(ZoneOffset.UTC).toLocalDateTime()
        } catch (e: DateTimeParseException) {
            null
        }
    }
}