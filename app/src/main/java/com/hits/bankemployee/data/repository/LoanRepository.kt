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
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.UUID

class LoanRepository(
    private val loanApi: LoanApi,
    private val mapper: LoanMapper,
) : ILoanRepository {

    override suspend fun getLoans(userId: String, pageInfo: PageInfo): Result<List<LoanEntity>> {
        //TODO
        delay(1000)
        return Result.Success(
            listOf(
                LoanEntity(
                    number = UUID.randomUUID().toString(),
                    tariff = LoanTariffEntity(
                        id = UUID.randomUUID().toString(),
                        name = "Тариф 1",
                        interestRate = "10.0",
                    ),
                    amount = "1000000",
                    termInMonths = 12,
                    bankAccountNumber = "12345678901234567890",
                    paymentAmount = "100000",
                    paymentSum = "1200000",
                    nextPaymentDateTime = LocalDateTime.now(),
                    currentDebt = "900000",
                ),
                LoanEntity(
                    number = UUID.randomUUID().toString(),
                    tariff = LoanTariffEntity(
                        id = UUID.randomUUID().toString(),
                        name = "Тариф 2",
                        interestRate = "15.0",
                    ),
                    amount = "2000000",
                    termInMonths = 24,
                    bankAccountNumber = "12345678901234567890",
                    paymentAmount = "100000",
                    paymentSum = "2400000",
                    nextPaymentDateTime = LocalDateTime.now(),
                    currentDebt = "1900000",
                ),
                LoanEntity(
                    number = UUID.randomUUID().toString(),
                    tariff = LoanTariffEntity(
                        id = UUID.randomUUID().toString(),
                        name = "Тариф 3",
                        interestRate = "20.0",
                    ),
                    amount = "3000000",
                    termInMonths = 36,
                    bankAccountNumber = "12345678901234567890",
                    paymentAmount = "100000",
                    paymentSum = "3600000",
                    nextPaymentDateTime = LocalDateTime.now(),
                    currentDebt = "2900000",
                ),
            )
        )
    }

    override suspend fun getLoanByNumber(loanNumber: String): Result<LoanEntity> {
        //TODO
        delay(1000)
        return Result.Success(data = LoanEntity(
            number = UUID.randomUUID().toString(),
            tariff = LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 1",
                interestRate = "10.0",
            ),
            amount = "1000000",
            termInMonths = 12,
            bankAccountNumber = "12345678901234567890",
            paymentAmount = "100000",
            paymentSum = "1200000",
            nextPaymentDateTime = LocalDateTime.now(),
            currentDebt = "900000",
        ))
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