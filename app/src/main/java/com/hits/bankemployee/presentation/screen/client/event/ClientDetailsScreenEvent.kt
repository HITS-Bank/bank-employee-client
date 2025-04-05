package com.hits.bankemployee.presentation.screen.client.event

import com.hits.bankemployee.presentation.screen.client.model.BankAccountStatus
import ru.hitsbank.bank_common.domain.entity.CurrencyCode

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