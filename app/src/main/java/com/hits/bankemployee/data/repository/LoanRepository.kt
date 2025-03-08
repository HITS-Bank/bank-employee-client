package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.LoanApi
import com.hits.bankemployee.data.common.apiCall
import com.hits.bankemployee.data.common.toCompletableResult
import com.hits.bankemployee.data.common.toResult
import com.hits.bankemployee.data.mapper.LoanMapper
import com.hits.bankemployee.data.model.loan.LoanTariffDeleteRequest
import com.hits.bankemployee.domain.common.Completable
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.domain.repository.ILoanRepository
import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import kotlinx.coroutines.Dispatchers

class LoanRepository(
    private val loanApi: LoanApi,
    private val mapper: LoanMapper,
) : ILoanRepository {

    override suspend fun getLoans(userId: String, pageInfo: PageInfo): Result<List<LoanEntity>> {
        //TODO
        return Result.Error()
    }

    override suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String?
    ): Result<List<LoanTariffEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanTariffs(
                sortingProperty = sortingProperty.name,
                sortingOrder = sortingOrder.name,
                pageNumber = pageInfo.pageNumber,
                pageSize = pageInfo.pageSize,
                nameQuery = query
            ).toResult { page ->
                page.loanTariffs.map { tariff ->
                    mapper.map(tariff)
                }
            }
        }
    }

    override suspend fun createLoanTariff(
        loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity
    ): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            loanApi.createLoanTariff(mapper.map(loanTariffCreateRequestEntity))
                .toCompletableResult()
        }
    }

    override suspend fun deleteLoanTariff(loanTariffId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            loanApi.deleteLoanTariff(LoanTariffDeleteRequest(loanTariffId))
                .toCompletableResult()
        }
    }
}