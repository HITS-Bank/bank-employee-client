package com.hits.bankemployee.core.domain.interactor

import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.common.toState
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.core.domain.entity.RoleType
import com.hits.bankemployee.core.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileInteractor(
    private val profileRepository: IProfileRepository
) {

    fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String? = null,
    ): Flow<State<List<ProfileEntity>>> = flow {
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