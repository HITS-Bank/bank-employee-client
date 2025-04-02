package com.hits.bankemployee.presentation.screen.loan.payments.model

import androidx.compose.ui.graphics.Color

data class LoanPaymentsState(
    val payments: List<LoanPaymentItem>,
)

data class LoanPaymentItem(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val currencyChar: Char,
    val foregroundColor: Color,
    val backgroundColor: Color,
)