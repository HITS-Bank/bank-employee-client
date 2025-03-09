package com.hits.bankemployee.data.model.account

data class AccountResponse(
    val accountNumber: String,
    val balance: String,
    val blocked: Boolean,
    val closed: Boolean,
)
