package com.hits.bankemployee.domain.repository

import com.hits.bankemployee.domain.common.Completable
import com.hits.bankemployee.domain.entity.ProfileEntity
import com.hits.bankemployee.domain.common.Result
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.domain.entity.RoleType

interface IProfileRepository {

    suspend fun getSelfProfile(): Result<ProfileEntity>

    suspend fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String? = null,
    ): Result<List<ProfileEntity>>

    suspend fun banUser(userId: String): Result<Completable>

    suspend fun unbanUser(userId: String): Result<Completable>

    suspend fun registerUser(request: RegisterRequestEntity): Result<Completable>
}