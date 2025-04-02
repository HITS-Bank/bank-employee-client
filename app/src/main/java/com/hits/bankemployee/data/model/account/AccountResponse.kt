package com.hits.bankemployee.data.model.account

import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode

data class AccountResponse(
    val accountId: String,
    val accountNumber: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val blocked: Boolean,
    val closed: Boolean,
)
