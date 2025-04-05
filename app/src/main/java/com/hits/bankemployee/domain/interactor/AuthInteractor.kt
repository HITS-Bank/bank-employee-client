package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.repository.IAuthRepository
import com.hits.bankemployee.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.toState
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: IAuthRepository,
    private val profileRepository: IProfileRepository,
) {

    fun exchangeAuthCodeForToken(code: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(authRepository.exchangeAuthCodeForToken(code).toState())

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