package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.core.domain.entity.bankaccount.OperationHistoryEntity

interface IBankAccountRepository {

    suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Result<List<BankAccountEntity>>

    suspend fun getAccountDetails(
        accountNumber: String
    ): Result<BankAccountEntity>

    suspend fun getAccountOperationHistory(
        accountNumber: String,
        pageInfo: PageInfo
    ): Result<List<OperationHistoryEntity>>
}