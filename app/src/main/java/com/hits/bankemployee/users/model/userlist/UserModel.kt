package com.hits.bankemployee.users.model.userlist

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hits.bankemployee.R
import com.hits.bankemployee.users.model.UserRole

data class UserModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBlocked: Boolean,
    val role: UserRole,
) {
    val fullName: String
        get() = "$firstName $lastName"

    val backgroundColor: Color
        @Composable get() = if (isBlocked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer

    val foregroundColor: Color
        @Composable get() = if (isBlocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer

    val subtitle: String
        get() = if (isBlocked) "Заблокирован" else role.title

    val swipeIconResId: Int
        get() = if (isBlocked) R.drawable.ic_unblock else R.drawable.ic_block
}
