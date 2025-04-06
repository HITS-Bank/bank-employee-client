package com.hits.bankemployee.presentation.screen.account.mapper

import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationTypeEntity
import com.hits.bankemployee.presentation.screen.account.model.AccountDetailsListItem
import ru.hitsbank.bank_common.presentation.common.formatToSum
import ru.hitsbank.bank_common.presentation.common.toSymbol
import ru.hitsbank.bank_common.presentation.common.utcDateTimeToReadableFormat
import ru.hitsbank.bank_common.presentation.theme.topUpBackground
import ru.hitsbank.bank_common.presentation.theme.topUpForeground
import ru.hitsbank.bank_common.presentation.theme.withdrawBackground
import ru.hitsbank.bank_common.presentation.theme.withdrawForeground
import javax.inject.Inject

class AccountDetailsScreenModelMapper @Inject constructor() {

    fun map(account: BankAccountEntity): List<AccountDetailsListItem.AccountDetailsProperty> {
        val secondProperty = when(account.status) {
            BankAccountStatusEntity.OPEN -> AccountDetailsListItem.AccountDetailsProperty(
                name = "Баланс",
                value = account.balance.formatToSum(account.currencyCode),
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
            date = operation.date.utcDateTimeToReadableFormat(),
            amount = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> "-${operation.amount.formatToSum(operation.currencyCode, true)}"
                OperationTypeEntity.TOP_UP -> "+${operation.amount.formatToSum(operation.currencyCode, true)}"
                OperationTypeEntity.LOAN_PAYMENT -> "-${operation.amount.formatToSum(operation.currencyCode, true)}"
                OperationTypeEntity.TRANSFER_INCOMING -> "+${operation.amount.formatToSum(operation.currencyCode, true)}"
                OperationTypeEntity.TRANSFER_OUTGOING -> "-${operation.amount.formatToSum(operation.currencyCode, true)}"
            },
            currencyChar = operation.currencyCode.toSymbol(),
            operationTitle = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> "Снятие"
                OperationTypeEntity.TOP_UP -> "Пополнение"
                OperationTypeEntity.LOAN_PAYMENT -> "Выплата по кредиту"
                OperationTypeEntity.TRANSFER_INCOMING -> "Входящий перевод"
                OperationTypeEntity.TRANSFER_OUTGOING -> "Исходящий перевод"
            },
            amountColor = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawForeground
                OperationTypeEntity.TOP_UP -> topUpForeground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawForeground
                OperationTypeEntity.TRANSFER_INCOMING -> topUpForeground
                OperationTypeEntity.TRANSFER_OUTGOING -> withdrawForeground
            },
            iconColor = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawForeground
                OperationTypeEntity.TOP_UP -> topUpForeground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawForeground
                OperationTypeEntity.TRANSFER_INCOMING -> topUpForeground
                OperationTypeEntity.TRANSFER_OUTGOING -> withdrawForeground
            },
            iconBackground = when (operation.type) {
                OperationTypeEntity.WITHDRAW -> withdrawBackground
                OperationTypeEntity.TOP_UP -> topUpBackground
                OperationTypeEntity.LOAN_PAYMENT -> withdrawBackground
                OperationTypeEntity.TRANSFER_INCOMING -> topUpBackground
                OperationTypeEntity.TRANSFER_OUTGOING -> withdrawBackground
            },
        )
    }
}