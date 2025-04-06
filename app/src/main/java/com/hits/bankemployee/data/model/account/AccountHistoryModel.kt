package com.hits.bankemployee.data.model.account

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class AccountHistoryModel(
    val id: String,
    val type: OperationType,
    val amount: String,
    val currencyCode: CurrencyCode,
    val executedAt: String,
)
