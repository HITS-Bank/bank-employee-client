package com.hits.bankemployee.core.domain.entity.loan

data class LoanTariffCreateRequestEntity(
    val name: String,
    val interestRate: String,
)
