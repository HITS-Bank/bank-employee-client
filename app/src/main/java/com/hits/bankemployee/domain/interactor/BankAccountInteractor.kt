package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.domain.entity.bankaccount.OperationHistoryEntity
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.toState
import javax.inject.Inject

class BankAccountInteractor @Inject constructor(
    private val bankAccountRepository: IBankAccountRepository,
) {

    fun getAccountList(
        userId: String,
        pageInfo: PageInfo,
    ): Flow<State<List<BankAccountEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountList(userId, pageInfo).toState())
    }

    fun getAccountDetails(
        accountId: String,
    ): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountDetails(accountId).toState())
    }

    fun getAccountOperationHistory(
        accountId: String,
        pageInfo: PageInfo,
    ): Flow<State<List<OperationHistoryEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountOperationHistory(accountId, pageInfo).toState())
    }

    fun getOperationHistoryUpdates(
        accountId: String,
    ): Flow<State<OperationHistoryEntity>> {
        return when (val flowResult = bankAccountRepository.getOperationHistoryUpdates(accountId)) {
            is Result.Error -> flowOf(State.Error(flowResult.throwable))
            is Result.Success -> flowResult.data.map<OperationHistoryEntity, State<OperationHistoryEntity>> { operation ->
                State.Success(operation)
            }.catch { throwable ->
                emit(State.Error(throwable))
            }
        }
    }
}