package com.hits.bankemployee.data.model

import ru.hitsbank.bank_common.domain.entity.RoleType

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: List<RoleType>,
)
