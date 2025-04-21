package com.hits.bankemployee.data.model.loan

data class LoanTariffCreateRequest(
    val requestId: String,
    val name: String,
    val interestRate: String,
)
