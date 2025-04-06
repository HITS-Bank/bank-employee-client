package com.hits.bankemployee.data.model

import ru.hitsbank.bank_common.domain.entity.RoleType

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>,
)
