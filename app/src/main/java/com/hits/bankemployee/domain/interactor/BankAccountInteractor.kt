package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.common.toState
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BankAccountInteractor(
    private val bankAccountRepository: IBankAccountRepository,
) {

    fun getAccountList(
        userId: String,
        pageInfo: PageInfo
    ): Flow<State<List<BankAccountEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountList(userId, pageInfo).toState())
    }

    fun getAccountDetails(
        accountNumber: String
    ): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountDetails(accountNumber).toState())
    }

    fun getAccountOperationHistory(
        accountNumber: String,
        pageInfo: PageInfo
    ): Flow<State<List<OperationHistoryEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountOperationHistory(accountNumber, pageInfo).toState())
    }
}