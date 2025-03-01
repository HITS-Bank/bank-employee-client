package com.hits.bankemployee.core.domain.entity

data class LoginRequestEntity(
    val email: String,
    val password: String,
)