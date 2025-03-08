package com.hits.bankemployee.presentation.screen.client.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed interface ClientDetailsListItem {

    data class BankAccountModel(
        val number: String,
        val description: String,
        val balance: String,
        val status: BankAccountStatus,
        private val descriptionColorProvider: @Composable () -> Color,
    ) : ClientDetailsListItem {
        val descriptionColor: Color
            @Composable get() = descriptionColorProvider()
    }

    data class LoanModel(
        val number: String,
        val description: String,
        private val descriptionColorProvider: @Composable () -> Color,
    ) : ClientDetailsListItem {
        val descriptionColor: Color
            @Composable get() = descriptionColorProvider()
    }

    data object AccountsHeader : ClientDetailsListItem

    data object LoansHeader : ClientDetailsListItem

}