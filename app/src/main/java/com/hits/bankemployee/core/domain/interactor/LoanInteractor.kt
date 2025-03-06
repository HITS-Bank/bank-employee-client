package com.hits.bankemployee.core.domain.interactor

import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.common.toState
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.core.domain.repository.ILoanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoanInteractor(private val loanRepository: ILoanRepository) {

    fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String? = null,
    ): Flow<State<List<LoanTariffEntity>>> = flow {
        emit(State.Loading)
        emit(
            loanRepository.getLoanTariffs(pageInfo, sortingProperty, sortingOrder, query).toState()
        )
    }

    fun createLoanTariff(
        loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity,
    ): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(loanRepository.createLoanTariff(loanTariffCreateRequestEntity).toState())
    }

    fun deleteLoanTariff(loanTariffId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(loanRepository.deleteLoanTariff(loanTariffId).toState())
    }
}