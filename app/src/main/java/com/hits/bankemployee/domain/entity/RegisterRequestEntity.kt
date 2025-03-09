package com.hits.bankemployee.domain.entity

data class RegisterRequestEntity(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: RoleType,
)
