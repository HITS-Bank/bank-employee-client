package com.hits.bankemployee.domain.entity

import ru.hitsbank.bank_common.domain.entity.RoleType

data class RegisterRequestEntity(
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: List<RoleType>,
)
