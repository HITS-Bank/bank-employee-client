package com.hits.bankemployee.data.model.loan

data class LoanResponse(
    val number: String,
    val tariff: LoanTariffResponse,
    val amount: String,
    val termInMonths: Int,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val nextPaymentDateTime: String,
    val currentDebt: String,
)
