package com.hits.bankemployee.presentation.screen.loan.details.event

sealed interface LoanDetailsEvent {

    data object Back : LoanDetailsEvent

    data class OpenBankAccount(val accountId: String) : LoanDetailsEvent
}