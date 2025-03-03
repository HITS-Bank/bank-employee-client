package com.hits.bankemployee.users.model.userlist

import com.hits.bankemployee.users.model.UserRole

data class UserModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBlocked: Boolean,
    val role: UserRole,
)
