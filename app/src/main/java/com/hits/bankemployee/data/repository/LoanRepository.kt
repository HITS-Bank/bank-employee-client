package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.LoanApi
import com.hits.bankemployee.data.mapper.LoanMapper
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanPaymentEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.domain.repository.ILoanRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.model.RequestIdHolder
import ru.hitsbank.bank_common.data.model.getNewRequestId
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toCompletableResult
import ru.hitsbank.bank_common.data.utils.toResult
import ru.hitsbank.bank_common.domain.Completable
import javax.inject.Inject
import javax.inject.Singleton
import ru.hitsbank.bank_common.domain.Result

@Singleton
class LoanRepository @Inject constructor(
    private val loanApi: LoanApi,
    private val mapper: LoanMapper,
) : ILoanRepository {

    private var createTariffIdHolder: RequestIdHolder? = null
    private var deleteTariffIdHolder: RequestIdHolder? = null

    override suspend fun getLoans(userId: String, pageInfo: PageInfo): Result<List<LoanEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getClientLoans(
                userId = userId,
                pageSize = pageInfo.pageSize,
                pageNumber = pageInfo.pageNumber,
            )
                .toResult { page ->
                    page.map { loan ->
                        mapper.map(loan)
                    }
                }
        }
    }

    override suspend fun getLoanById(loanId: String): Result<LoanEntity> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanById(loanId).toResult { loan ->
                mapper.map(loan)
            }
        }
    }

    override suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String?,
    ): Result<List<LoanTariffEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanTariffs(
                sortingProperty = sortingProperty.name,
                sortingOrder = sortingOrder.name,
                pageNumber = pageInfo.pageNumber,
                pageSize = pageInfo.pageSize,
                nameQuery = query,
            ).toResult { page ->
                page.map { tariff ->
                    mapper.map(tariff)
                }
            }
        }
    }

    override suspend fun createLoanTariff(
        loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity,
    ): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            val idHolder = createTariffIdHolder.getNewRequestId(loanTariffCreateRequestEntity.hashCode())
            createTariffIdHolder = idHolder
            loanApi.createLoanTariff(mapper.map(idHolder.requestId, loanTariffCreateRequestEntity))
                .also { response ->
                    if (response.isSuccessful) {
                        createTariffIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun deleteLoanTariff(loanTariffId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            val idHolder = deleteTariffIdHolder.getNewRequestId(loanTariffId.hashCode())
            deleteTariffIdHolder = idHolder
            loanApi.deleteLoanTariff(loanTariffId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        deleteTariffIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun getLoanRating(userId: String): Result<Int> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanUserRating(userId).toResult { rating ->
                rating.rating
            }
        }
    }

    override suspend fun getLoanPayments(loanId: String): Result<List<LoanPaymentEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanPayments(loanId).toResult { page ->
                page.map { payment ->
                    mapper.map(payment)
                }
            }
        }
    }
}