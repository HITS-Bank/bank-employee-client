package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity

interface IBankAccountRepository {

    suspend fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Result<List<BankAccountEntity>>
}