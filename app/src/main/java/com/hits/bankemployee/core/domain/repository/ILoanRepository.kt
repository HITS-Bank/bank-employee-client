package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.entity.loan.LoanEntity

interface ILoanRepository {

    suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String? = null,
    ): Result<List<LoanTariffEntity>>

    suspend fun getLoans(
        userId: String,
        pageInfo: PageInfo,
    ): Result<List<LoanEntity>>

    suspend fun createLoanTariff(loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity): Result<Completable>

    suspend fun deleteLoanTariff(loanTariffId: String): Result<Completable>
}