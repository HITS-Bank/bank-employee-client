package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.account.AccountHistoryModel
import com.hits.bankemployee.data.model.account.AccountResponse
import com.hits.bankemployee.data.model.account.OperationType
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationTypeEntity
import ru.hitsbank.bank_common.data.model.OperationResponse
import ru.hitsbank.bank_common.data.model.OperationTypeResponse
import javax.inject.Inject

class BankAccountMapper @Inject constructor() {

    fun map(account: AccountResponse): BankAccountEntity {
        return BankAccountEntity(
            id = account.accountId,
            number = account.accountNumber,
            balance = account.balance,
            currencyCode = account.currencyCode,
            status = if (account.blocked) BankAccountStatusEntity.BLOCKED else if (account.closed) BankAccountStatusEntity.CLOSED else BankAccountStatusEntity.OPEN,
        )
    }

    fun map(operation: AccountHistoryModel): OperationHistoryEntity {
        return OperationHistoryEntity(
            id = operation.id,
            date = operation.executedAt,
            amount = operation.amount,
            currencyCode = operation.currencyCode,
            type = when (operation.type) {
                OperationType.TOP_UP -> OperationTypeEntity.TOP_UP
                OperationType.WITHDRAW -> OperationTypeEntity.WITHDRAW
                OperationType.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
                OperationType.TRANSFER_INCOMING -> OperationTypeEntity.TRANSFER_INCOMING
                OperationType.TRANSFER_OUTGOING -> OperationTypeEntity.TRANSFER_OUTGOING
            },
        )
    }

    fun map(response: OperationResponse): OperationHistoryEntity {
        return OperationHistoryEntity(
            id = response.id,
            date = response.executedAt,
            type = when (response.type) {
                OperationTypeResponse.WITHDRAW -> OperationTypeEntity.WITHDRAW
                OperationTypeResponse.TOP_UP -> OperationTypeEntity.TOP_UP
                OperationTypeResponse.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
                OperationTypeResponse.TRANSFER_INCOMING -> OperationTypeEntity.TRANSFER_INCOMING
                OperationTypeResponse.TRANSFER_OUTGOING -> OperationTypeEntity.TRANSFER_OUTGOING
            },
            amount = response.amount,
            currencyCode = response.currencyCode,
        )
    }
}