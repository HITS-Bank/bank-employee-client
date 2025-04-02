package com.hits.bankemployee.presentation.screen.loan.payments.event

sealed interface LoanPaymentsEvent {

    data object Back : LoanPaymentsEvent

    data object Refresh : LoanPaymentsEvent

    data object LoanInfoClick : LoanPaymentsEvent
}