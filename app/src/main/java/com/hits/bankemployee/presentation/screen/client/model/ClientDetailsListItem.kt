package com.hits.bankemployee.presentation.screen.client.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode

sealed interface ClientDetailsListItem {

    data class BankAccountModel(
        val id: String,
        val number: String,
        val description: String,
        val balance: String,
        val currencyCode: CurrencyCode,
        val status: BankAccountStatus,
        private val descriptionColorProvider: @Composable () -> Color,
    ) : ClientDetailsListItem {
        val descriptionColor: Color
            @Composable get() = descriptionColorProvider()
    }

    data class LoanModel(
        val id: String,
        val number: String,
        val description: String,
        private val descriptionColorProvider: @Composable () -> Color,
    ) : ClientDetailsListItem {
        val descriptionColor: Color
            @Composable get() = descriptionColorProvider()
    }

    data object UserInfoHeader : ClientDetailsListItem

    data class LoanRatingModel(
        val rating: String,
    ) : ClientDetailsListItem

    data object IsBlockedModel : ClientDetailsListItem

    data class RolesModel(
        val roles: List<String>,
    ) : ClientDetailsListItem {

        val rolesText: String
            get() = roles.joinToString(separator = ", ")
    }

    data object AccountsHeader : ClientDetailsListItem

    data object LoansHeader : ClientDetailsListItem

}