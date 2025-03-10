package com.hits.bankemployee.presentation.screen.client.event

import com.hits.bankemployee.presentation.screen.client.model.BankAccountStatus

sealed interface ClientDetailsScreenEvent {

    data object FabClicked : ClientDetailsScreenEvent

    data object DialogConfirmed : ClientDetailsScreenEvent

    data object DialogDismissed : ClientDetailsScreenEvent

    data class BankAccountClicked(val number: String, val balance: String, val status: BankAccountStatus) :
        ClientDetailsScreenEvent

    data class LoanClicked(val number: String) : ClientDetailsScreenEvent

    data object NavigateBack : ClientDetailsScreenEvent
}