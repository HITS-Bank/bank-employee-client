package com.hits.bankemployee.presentation.navigation

import com.hits.bankemployee.R
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.presentation.navigation.BottomBarDestination
import ru.hitsbank.bank_common.presentation.navigation.Destination

object Auth : Destination() {
    const val OPTIONAL_AUTH_CODE_ARG = "authCode"

    fun withArgs(authCode: String?): String {
        return destinationWithArgs(
            args = emptyList(),
            optionalArgs = mapOf(
                OPTIONAL_AUTH_CODE_ARG to authCode,
            )
        )
    }

    override var optionalArguments = listOf(
        OPTIONAL_AUTH_CODE_ARG,
    )
}

object BottomBarRoot : Destination()

object Users : BottomBarDestination() {
    override val icon = R.drawable.ic_users
    override val title = "Пользователи"
}

object Tariffs : BottomBarDestination() {
    override val icon = R.drawable.ic_loan
    override val title = "Кредиты"
}

object Personalization : BottomBarDestination() {
    override val icon = R.drawable.ic_personalization
    override val title = "Персонализация"
}

object UserDetails : Destination() {
    const val ARG_USER_ID = "userId"
    const val ARG_USER_FULLNAME = "userFullname"
    const val ARG_IS_USER_BLOCKED = "isUserBlocked"
    const val ARG_IS_CLIENT = "isClient"
    const val ARG_IS_EMPLOYEE = "isEmployee"

    override var arguments = listOf(ARG_USER_ID, ARG_USER_FULLNAME, ARG_IS_USER_BLOCKED, ARG_IS_CLIENT, ARG_IS_EMPLOYEE)

    fun withArgs(
        userId: String,
        userFullname: String,
        isUserBlocked: Boolean,
        userRoles: List<RoleType>,
    ): String = destinationWithArgs(userId, userFullname, isUserBlocked, userRoles.contains(RoleType.CLIENT), userRoles.contains(RoleType.EMPLOYEE))
}

object BankAccountDetails : Destination() {
    const val ARG_BANK_ACCOUNT_ID = "bankAccountId"
    const val ARG_BANK_ACCOUNT_NUMBER = "bankAccountNumber"
    const val ARG_BANK_ACCOUNT_BALANCE = "bankAccountBalance"
    const val ARG_CURRENCY_CODE = "currencyCode"
    const val ARG_BANK_ACCOUNT_STATUS = "bankAccountStatus"

    override var arguments = listOf(ARG_BANK_ACCOUNT_ID)
    override var optionalArguments = listOf(
        ARG_BANK_ACCOUNT_NUMBER,
        ARG_BANK_ACCOUNT_BALANCE,
        ARG_CURRENCY_CODE,
        ARG_BANK_ACCOUNT_STATUS,
    )

    fun withArgs(
        bankAccountId: String,
        bankAccountNumber: String? = null,
        bankAccountBalance: String? = null,
        currencyCode: String? = null,
        bankAccountStatus: String? = null,
    ): String {
        val optionalArgs = mutableMapOf<String, String>()
        bankAccountNumber?.let { optionalArgs[ARG_BANK_ACCOUNT_NUMBER] = it }
        bankAccountBalance?.let { optionalArgs[ARG_BANK_ACCOUNT_BALANCE] = it }
        currencyCode?.let { optionalArgs[ARG_CURRENCY_CODE] = it }
        bankAccountStatus?.let { optionalArgs[ARG_BANK_ACCOUNT_STATUS] = it }
        return destinationWithArgs(
            listOf(bankAccountId),
            optionalArgs,
        )
    }
}

object LoanDetails : Destination() {
    const val ARG_LOAN_ID = "loanId"

    override var arguments = listOf(ARG_LOAN_ID)
}

object LoanPayments : Destination() {
    const val ARG_LOAN_ID = "loanId"

    override var arguments = listOf(ARG_LOAN_ID)
}