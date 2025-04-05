package com.hits.bankemployee.domain.repository

import com.hits.bankemployee.domain.entity.UserEntity
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.entity.RoleType

interface IUserRepository {

    suspend fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String? = null,
    ): Result<List<UserEntity>>

    suspend fun banUser(userId: String): Result<Completable>

    suspend fun unbanUser(userId: String): Result<Completable>

    suspend fun registerUser(request: RegisterRequestEntity): Result<Completable>
}