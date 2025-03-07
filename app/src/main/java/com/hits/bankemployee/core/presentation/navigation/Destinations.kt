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
    const val ARG_USER_FULLNAME = "userFullname"
    const val ARG_IS_USER_BLOCKED = "isUserBlocked"

    override var arguments = listOf(ARG_USER_ID, ARG_USER_FULLNAME, ARG_IS_USER_BLOCKED)

    fun withArgs(
        userId: String,
        userFullname: String,
        isUserBlocked: Boolean,
    ): String = destinationWithArgs(userId, userFullname, isUserBlocked)
}

object BankAccountDetails : Destination() {
    const val ARG_BANK_ACCOUNT_NUMBER = "bankAccountNumber"
    const val ARG_BANK_ACCOUNT_BALANCE = "bankAccountBalance"
    const val ARG_BANK_ACCOUNT_STATUS = "bankAccountStatus"

    override var arguments = listOf(ARG_BANK_ACCOUNT_NUMBER)
    override var optionalArguments = listOf(ARG_BANK_ACCOUNT_BALANCE, ARG_BANK_ACCOUNT_STATUS)

    fun withArgs(
        bankAccountNumber: String,
        bankAccountBalance: String? = null,
        bankAccountStatus: String? = null,
    ): String {
        val optionalArgs = mutableMapOf<String, String>()
        bankAccountBalance?.let { optionalArgs[ARG_BANK_ACCOUNT_BALANCE] = it }
        bankAccountStatus?.let { optionalArgs[ARG_BANK_ACCOUNT_STATUS] = it }
        return destinationWithArgs(
            listOf(bankAccountNumber),
            optionalArgs,
        )
    }
}

object LoanDetails : Destination() {
    const val ARG_LOAN_NUMBER = "loanNumber"

    override var arguments = listOf(ARG_LOAN_NUMBER)
}