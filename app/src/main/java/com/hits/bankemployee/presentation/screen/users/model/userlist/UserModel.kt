package com.hits.bankemployee.presentation.screen.users.model.userlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.hitsbank.bank_common.domain.entity.RoleType

data class UserModel(
    val id: String,
    val isBlocked: Boolean,
    val fullName: String,
    val status: String,
    val roles: List<RoleType>,
    val actionIconResId: Int,
    private val backgroundColorProvider: @Composable () -> Color,
    private val foregroundColorProvider: @Composable () -> Color,
) {
    val backgroundColor: Color
        @Composable get() = backgroundColorProvider()

    val foregroundColor: Color
        @Composable get() = foregroundColorProvider()
}
