package com.hits.bankemployee.domain.entity.bankaccount

data class OperationHistoryEntity(
    val id: String,
    val date: String,
    val amount: String,
    val currencyCode: CurrencyCode,
    val type: OperationTypeEntity,
)
