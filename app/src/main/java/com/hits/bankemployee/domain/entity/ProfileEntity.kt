package com.hits.bankemployee.domain.entity

data class ProfileEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBanned: Boolean,
    val roles: List<RoleType>,
)