package com.hits.bankemployee.domain.entity.loan

import java.time.LocalDateTime

data class LoanEntity(
    val number: String,
    val tariff: LoanTariffEntity,
    val amount: String,
    val termInMonths: Int,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val nextPaymentDateTime: LocalDateTime,
    val currentDebt: String,
)
