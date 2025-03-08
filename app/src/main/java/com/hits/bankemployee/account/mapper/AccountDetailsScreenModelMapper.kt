package com.hits.bankemployee.account.mapper

import com.hits.bankemployee.account.model.AccountDetailsListItem
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.core.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.core.domain.entity.bankaccount.OperationTypeEntity
import com.hits.bankemployee.core.presentation.theme.topUpBackground
import com.hits.bankemployee.core.presentation.theme.topUpForeground
import com.hits.bankemployee.core.presentation.theme.withdrawBackground
import com.hits.bankemployee.core.presentation.theme.withdrawForeground
import java.time.format.DateTimeFormatter
import java.util.Locale

class AccountDetailsScreenModelMapper {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMMM yyyy", Locale("ru"))

    fun map(account: BankAccountEntity): List<AccountDetailsListItem.AccountDetailsProperty> {
        val secondProperty = when(account.status) {
            BankAccountStatusEntity.OPEN -> AccountDetailsListItem.AccountDetailsProperty(
                name = "Баланс",
                value = "${account.balance} ₽",
            )
            BankAccountStatusEntity.CLOSED -> AccountDetailsListItem.AccountDetailsProperty(
                name = "Статус",
                value = "Закрыт",
            )
            BankAccountStatusEntity.BLOCKED -> AccountDetailsListItem.AccountDetailsProperty(
                name = "Статус",
                value = "Заблокирован",
            )
        }
        return listOf(
            AccountDetailsListItem.AccountDetailsProperty(
                name = "Номер счета",
                value = account.number,
            ),
            secondProperty,
        )
    }

    fun map(operation: OperationHistoryEntity): AccountDetailsListItem.OperationHistoryItem {
        return AccountDetailsListItem.OperationHistoryItem(
            id = operation.id,
            date = formatter.format(operation.date),
            amount = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> "-${operation.amount} ₽"
                OperationTypeEntity.TOP_UP -> "+${operation.amount} ₽"
                OperationTypeEntity.LOAN_PAYMENT -> "-${operation.amount} ₽"
            },
            operationTitle = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> "Снятие"
                OperationTypeEntity.TOP_UP -> "Пополнение"
                OperationTypeEntity.LOAN_PAYMENT -> "Выплата по кредиту"
            },
            amountColor = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawForeground
                OperationTypeEntity.TOP_UP -> topUpForeground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawForeground
            },
            iconColor = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawForeground
                OperationTypeEntity.TOP_UP -> topUpForeground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawForeground
            },
            iconBackground = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawBackground
                OperationTypeEntity.TOP_UP -> topUpBackground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawBackground
            },
        )
    }
}