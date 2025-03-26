package com.hits.bankemployee.domain.repository

import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity

interface IBankAccountRepository {

    suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo,
    ): Result<List<BankAccountEntity>>

    suspend fun getAccountDetails(
        accountId: String,
    ): Result<BankAccountEntity>

    suspend fun getAccountOperationHistory(
        accountId: String,
        pageInfo: PageInfo,
    ): Result<List<OperationHistoryEntity>>
}