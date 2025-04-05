package com.hits.bankemployee.domain.entity

import ru.hitsbank.bank_common.domain.entity.RoleType

data class UserEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBanned: Boolean,
    val roles: List<RoleType>,
)