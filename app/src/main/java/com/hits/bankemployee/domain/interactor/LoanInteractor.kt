package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.common.Completable
import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.common.toState
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.domain.repository.ILoanRepository
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

    fun getLoans(userId: String, pageInfo: PageInfo): Flow<State<List<LoanEntity>>> = flow {
        emit(State.Loading)
        emit(loanRepository.getLoans(userId, pageInfo).toState())
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