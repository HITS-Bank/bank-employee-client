package com.hits.bankemployee.domain.entity.bankaccount

import java.time.LocalDateTime

data class OperationHistoryEntity(
    val id: String,
    val date: LocalDateTime,
    val amount: String,
    val currencyCode: CurrencyCode,
    val type: OperationTypeEntity,
)
