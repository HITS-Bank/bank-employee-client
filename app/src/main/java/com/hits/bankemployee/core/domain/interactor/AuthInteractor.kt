package com.hits.bankemployee.core.domain.interactor

import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.common.toState
import com.hits.bankemployee.core.domain.entity.LoginRequestEntity
import com.hits.bankemployee.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthInteractor(
    private val authRepository: IAuthRepository,
) {

    fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(authRepository.login(channel, request).toState())
    }
}