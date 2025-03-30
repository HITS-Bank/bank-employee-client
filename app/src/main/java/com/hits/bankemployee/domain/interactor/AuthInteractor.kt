package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.common.Completable
import com.hits.bankemployee.domain.common.State
import com.hits.bankemployee.domain.common.toState
import com.hits.bankemployee.domain.entity.LoginRequestEntity
import com.hits.bankemployee.domain.repository.IAuthRepository
import com.hits.bankemployee.domain.repository.IProfileRepository
import com.hits.bankemployee.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: IAuthRepository,
    private val profileRepository: IProfileRepository,
) {

    fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Flow<State<Completable>> = flow {
        emit(State.Loading)
        val loginState = authRepository.login(channel, request).toState()
        emit(loginState)

        if (loginState !is State.Success) {
            return@flow
        }

        when (val userProfile = profileRepository.getSelfProfile()) {
            is Result.Error -> emit(userProfile.toState())
            is Result.Success -> authRepository.saveIsUserBlocked(userProfile.data.isBanned)
        }
    }

    fun getIsUserBlocked(): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        emit(authRepository.getIsUserBlocked().toState())
    }
}