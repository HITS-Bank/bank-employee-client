package com.hits.bankemployee.domain.entity.bankaccount

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class OperationHistoryEntity(
    val id: String,
    val date: String,
    val amount: String,
    val currencyCode: CurrencyCode,
    val type: OperationTypeEntity,
)
