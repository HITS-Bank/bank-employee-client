package com.hits.bankemployee.domain.repository

import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingProperty
import ru.hitsbank.bank_common.domain.Result
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanPaymentEntity
import ru.hitsbank.bank_common.domain.Completable

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

    suspend fun getLoanById(
        loanId: String,
    ): Result<LoanEntity>

    suspend fun createLoanTariff(loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity): Result<Completable>

    suspend fun deleteLoanTariff(loanTariffId: String): Result<Completable>

    suspend fun getLoanRating(userId: String): Result<Int>

    suspend fun getLoanPayments(
        loanId: String,
    ): Result<List<LoanPaymentEntity>>
}