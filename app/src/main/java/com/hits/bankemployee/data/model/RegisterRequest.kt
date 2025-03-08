package com.hits.bankemployee.data.model

import com.hits.bankemployee.domain.entity.RoleType

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: RoleType,
)
