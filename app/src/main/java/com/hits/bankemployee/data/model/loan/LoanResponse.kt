package com.hits.bankemployee.data.model.loan

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class LoanResponse(
    val id: String,
    val number: String,
    val tariff: LoanTariffResponse,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val currencyCode: CurrencyCode,
    val nextPaymentDateTime: String,
    val currentDebt: String,
)
