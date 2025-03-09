package com.hits.bankemployee.domain.entity

data class ProfileEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBanned: Boolean,
    val email: String,
    val role: RoleType,
)