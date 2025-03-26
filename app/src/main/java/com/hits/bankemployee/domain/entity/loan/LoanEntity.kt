package com.hits.bankemployee.domain.entity.loan

import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode
import java.time.LocalDateTime

data class LoanEntity(
    val id: String,
    val number: String,
    val tariff: LoanTariffEntity,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val currencyCode: CurrencyCode,
    val nextPaymentDateTime: LocalDateTime,
    val currentDebt: String,
)
