package com.hits.bankemployee.core.data.repository

import com.hits.bankemployee.core.data.api.ProfileApi
import com.hits.bankemployee.core.data.common.apiCall
import com.hits.bankemployee.core.data.common.toCompletableResult
import com.hits.bankemployee.core.data.common.toResult
import com.hits.bankemployee.core.data.datasource.SessionManager
import com.hits.bankemployee.core.data.mapper.ProfileMapper
import com.hits.bankemployee.core.domain.common.Completable
import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.common.map
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.core.domain.entity.RoleType
import com.hits.bankemployee.core.domain.repository.IProfileRepository
import kotlinx.coroutines.Dispatchers

class ProfileRepository(
    private val profileApi: ProfileApi,
    private val sessionManager: SessionManager,
    private val mapper: ProfileMapper,
) : IProfileRepository {

    override suspend fun getSelfProfile(): Result<ProfileEntity> {
        return apiCall(Dispatchers.IO) {
            profileApi.getSelfProfile()
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveIsUserBlocked(result.data.isBanned)
                    }
                }
                .map(mapper::map)
        }
    }

    override suspend fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String?
    ): Result<List<ProfileEntity>> {
        return apiCall(Dispatchers.IO) {
            profileApi.getProfilesPage(roleType.name, page.pageNumber, page.pageSize, query)
                .toResult()
                .map { it.map { mapper.map(it) } }
        }
    }

    override suspend fun banUser(userId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            profileApi.banUser(userId)
                .toCompletableResult()
        }
    }

    override suspend fun unbanUser(userId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            profileApi.unbanUser(userId)
                .toCompletableResult()
        }
    }

    override suspend fun registerUser(request: RegisterRequestEntity): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            profileApi.registerUser(mapper.map(request))
                .toCompletableResult()
        }
    }
}