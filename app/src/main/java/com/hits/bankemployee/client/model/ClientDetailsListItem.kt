package com.hits.bankemployee.client.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

sealed interface ClientDetailsListItem {

    data class BankAccountModel(
        val number: String,
        val balance: String,
        val status: BankAccountStatus,
    ) : ClientDetailsListItem {
        val description
            get() = when (status) {
                BankAccountStatus.OPEN -> "Баланс: $balance ₽"
                BankAccountStatus.CLOSED -> "Закрыт"
                BankAccountStatus.BLOCKED -> "Заблокирован"
            }

        val descriptionColor
            @Composable get() = when (status) {
                BankAccountStatus.OPEN -> MaterialTheme.colorScheme.onSurfaceVariant
                BankAccountStatus.CLOSED -> MaterialTheme.colorScheme.onPrimaryContainer
                BankAccountStatus.BLOCKED -> MaterialTheme.colorScheme.onErrorContainer
            }
    }

    data class LoanModel(
        val number: String,
        val currentDebt: String,
    ) : ClientDetailsListItem {
        val description
            get() = "Долг: $currentDebt ₽"

        val descriptionColor
            @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    }

    data object AccountsHeader : ClientDetailsListItem

    data object LoansHeader : ClientDetailsListItem

}