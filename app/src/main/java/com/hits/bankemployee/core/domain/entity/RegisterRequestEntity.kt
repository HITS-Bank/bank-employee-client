package com.hits.bankemployee.core.domain.entity

data class RegisterRequestEntity(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: RoleType,
)
