package com.hits.bankemployee.data.repository

import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationTypeEntity
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.UUID

class BankAccountRepository : IBankAccountRepository {

    override suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Result<List<BankAccountEntity>> {
        //TODO
        delay(1000)
        return Result.Success(
            listOf(
                BankAccountEntity(
                    number = UUID.randomUUID().toString(),
                    balance = "1000.0",
                    status = BankAccountStatusEntity.OPEN,
                ),
                BankAccountEntity(
                    number = UUID.randomUUID().toString(),
                    balance = "2000.0",
                    status = BankAccountStatusEntity.CLOSED,
                ),
                BankAccountEntity(
                    number = UUID.randomUUID().toString(),
                    balance = "3000.0",
                    status = BankAccountStatusEntity.OPEN,
                ),
                BankAccountEntity(
                    number = UUID.randomUUID().toString(),
                    balance = "4000.0",
                    status = BankAccountStatusEntity.BLOCKED,
                ),
                BankAccountEntity(
                    number = UUID.randomUUID().toString(),
                    balance = "5000.0",
                    status = BankAccountStatusEntity.OPEN,
                )
            )
        )
    }

    override suspend fun getAccountDetails(accountNumber: String): Result<BankAccountEntity> {
        //TODO
        delay(1000)
        return Result.Success(
            BankAccountEntity(
                number = UUID.randomUUID().toString(),
                balance = "5000.0",
                status = BankAccountStatusEntity.OPEN,
            )
        )
    }

    override suspend fun getAccountOperationHistory(
        accountNumber: String,
        pageInfo: PageInfo
    ): Result<List<OperationHistoryEntity>> {
        //TODO
        delay(1000)
        return Result.Success(
            listOf(
                OperationHistoryEntity(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now(),
                    amount = "100.0",
                    type = OperationTypeEntity.TOP_UP,
                ),
                OperationHistoryEntity(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now(),
                    amount = "200.0",
                    type = OperationTypeEntity.WITHDRAW,
                ),
                OperationHistoryEntity(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now(),
                    amount = "300.0",
                    type = OperationTypeEntity.LOAN_PAYMENT,
                ),
                OperationHistoryEntity(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now(),
                    amount = "400.0",
                    type = OperationTypeEntity.TOP_UP,
                ),
                OperationHistoryEntity(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now(),
                    amount = "500.0",
                    type = OperationTypeEntity.WITHDRAW,
                ),
            )
        )
    }
}