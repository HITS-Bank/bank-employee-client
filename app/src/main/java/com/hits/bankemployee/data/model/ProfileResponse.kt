package com.hits.bankemployee.data.model

import com.hits.bankemployee.domain.entity.RoleType

data class ProfileResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>,
)
