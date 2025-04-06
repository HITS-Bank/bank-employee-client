package com.hits.bankemployee.data.model.loan

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import com.hits.bankemployee.domain.entity.loan.LoanPaymentStatus

data class LoanPaymentResponse(
    val id: String,
    val status: LoanPaymentStatus,
    val dateTime: String,
    val amount: String,
    val currencyCode: CurrencyCode,
)
