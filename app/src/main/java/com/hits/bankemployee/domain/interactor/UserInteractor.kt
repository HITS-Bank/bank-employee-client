package com.hits.bankemployee.domain.interactor

import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.UserEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.domain.toState
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val profileRepository: IUserRepository
) {

    fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String? = null,
    ): Flow<State<List<UserEntity>>> = flow {
        emit(State.Loading)
        emit(profileRepository.getProfilesPage(roleType, page, query).toState())
    }

    fun banUser(userId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(profileRepository.banUser(userId).toState())
    }

    fun unbanUser(userId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(profileRepository.unbanUser(userId).toState())
    }

    fun registerUser(request: RegisterRequestEntity): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(profileRepository.registerUser(request).toState())
    }
}