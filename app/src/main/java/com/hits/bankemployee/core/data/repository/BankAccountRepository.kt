package com.hits.bankemployee.core.data.repository

import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.core.domain.repository.IBankAccountRepository

class BankAccountRepository : IBankAccountRepository {

    override suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Result<List<BankAccountEntity>> {
        //TODO
        return Result.Error()
    }
}