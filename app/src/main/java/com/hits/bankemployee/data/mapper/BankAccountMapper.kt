package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.account.AccountHistoryModel
import com.hits.bankemployee.data.model.account.AccountResponse
import com.hits.bankemployee.data.model.account.OperationType
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationTypeEntity
import java.time.LocalDateTime

class BankAccountMapper {

    fun map(account: AccountResponse): BankAccountEntity {
        return BankAccountEntity(
            number = account.accountNumber,
            balance = account.balance,
            status = if (account.blocked) BankAccountStatusEntity.BLOCKED else if (account.closed) BankAccountStatusEntity.CLOSED else BankAccountStatusEntity.OPEN
        )
    }

    fun map(operation: AccountHistoryModel): OperationHistoryEntity {
        return OperationHistoryEntity(
            id = operation.id,
            date = LocalDateTime.parse(operation.executedAt),
            amount = operation.amount,
            type = when (operation.type) {
                OperationType.TOP_UP -> OperationTypeEntity.TOP_UP
                OperationType.WITHDRAW -> OperationTypeEntity.WITHDRAW
                OperationType.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
            }
        )
    }
}