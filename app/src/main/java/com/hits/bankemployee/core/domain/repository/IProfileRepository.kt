package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.core.domain.entity.RoleType

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