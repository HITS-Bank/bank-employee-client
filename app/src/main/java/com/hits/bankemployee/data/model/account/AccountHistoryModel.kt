package com.hits.bankemployee.data.model.account

import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode

data class AccountHistoryModel(
    val id: String,
    val type: OperationType,
    val amount: String,
    val currencyCode: CurrencyCode,
    val executedAt: String,
)
