package com.hits.bankemployee.presentation.screen.users.model.userlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class UserModel(
    val id: String,
    val isBlocked: Boolean,
    val fullName: String,
    val status: String,
    val actionIconResId: Int,
    private val backgroundColorProvider: @Composable () -> Color,
    private val foregroundColorProvider: @Composable () -> Color,
) {
    val backgroundColor: Color
        @Composable get() = backgroundColorProvider()

    val foregroundColor: Color
        @Composable get() = foregroundColorProvider()
}
