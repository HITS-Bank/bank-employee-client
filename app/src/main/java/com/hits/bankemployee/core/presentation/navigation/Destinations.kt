package com.hits.bankemployee.core.presentation.navigation

import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.navigation.base.BottomBarDestination
import com.hits.bankemployee.core.presentation.navigation.base.Destination

object Auth : Destination()

object BottomBarRoot : Destination()

object Users : BottomBarDestination() {
    override val icon = R.drawable.ic_users
    override val title = "Пользователи"
}

object Tariffs : BottomBarDestination() {
    override val icon = R.drawable.ic_loan
    override val title = "Кредиты"
}

object UserDetails : Destination() {
    const val ARG_USER_ID = "userId"

    override var arguments = listOf(ARG_USER_ID)
}

object BankAccountDetails : Destination() {
    const val ARG_BANK_ACCOUNT_NUMBER = "bankAccountId"

    override var arguments = listOf(ARG_BANK_ACCOUNT_NUMBER)
}

object LoanDetails : Destination() {
    const val ARG_LOAN_NUMBER = "loanId"

    override var arguments = listOf(ARG_LOAN_NUMBER)
}