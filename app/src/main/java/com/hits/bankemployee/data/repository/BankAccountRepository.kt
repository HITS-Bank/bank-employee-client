package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.BankAccountApi
import com.hits.bankemployee.data.common.apiCall
import com.hits.bankemployee.data.common.toResult
import com.hits.bankemployee.data.mapper.BankAccountMapper
import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.common.map
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import kotlinx.coroutines.Dispatchers

class BankAccountRepository(
    private val bankAccountApi: BankAccountApi,
    private val mapper: BankAccountMapper,
) : IBankAccountRepository {

    override suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Result<List<BankAccountEntity>> {
        if (pageInfo.pageNumber > 1)
            return Result.Success(emptyList())
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getAccountsList(
                clientId = userId,
                pageNumber = pageInfo.pageNumber,
                pageSize = pageInfo.pageSize,
            )
                .toResult()
                .map { accounts ->
                    accounts.map { account ->
                        mapper.map(account)
                    }
                }
        }
    }

    override suspend fun getAccountDetails(accountId: String): Result<BankAccountEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getAccountById(accountId)
                .toResult()
                .map { account ->
                    mapper.map(account)
                }
        }
    }

    override suspend fun getAccountOperationHistory(
        accountId: String,
        pageInfo: PageInfo
    ): Result<List<OperationHistoryEntity>> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getAccountOperationHistory(
                accountId = accountId,
                pageNumber = pageInfo.pageNumber,
                pageSize = pageInfo.pageSize,
            ).toResult().map { list ->
                list.map { operation -> mapper.map(operation) }
            }
        }
    }
}