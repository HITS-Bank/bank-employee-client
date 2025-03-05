package com.hits.bankemployee.core.data.model

import com.hits.bankemployee.core.domain.entity.RoleType

data class ProfileResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBanned: Boolean,
    val email: String,
    val role: RoleType,
)
