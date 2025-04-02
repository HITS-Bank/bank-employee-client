package com.hits.bankemployee.presentation.screen.client.event

import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode
import com.hits.bankemployee.presentation.screen.client.model.BankAccountStatus

sealed interface ClientDetailsScreenEvent {

    data object FabClicked : ClientDetailsScreenEvent

    data object DialogConfirmed : ClientDetailsScreenEvent

    data object DialogDismissed : ClientDetailsScreenEvent

    data class BankAccountClicked(
        val id: String,
        val number: String,
        val balance: String,
        val currencyCode: CurrencyCode,
        val status: BankAccountStatus,
    ) : ClientDetailsScreenEvent

    data class LoanClicked(val id: String) : ClientDetailsScreenEvent

    data object NavigateBack : ClientDetailsScreenEvent
}