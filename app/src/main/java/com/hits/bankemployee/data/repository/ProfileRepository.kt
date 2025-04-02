package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.ProfileApi
import com.hits.bankemployee.data.datasource.SessionManager
import com.hits.bankemployee.data.mapper.ProfileMapper
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.ProfileEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.domain.entity.RoleType
import com.hits.bankemployee.domain.repository.IProfileRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toCompletableResult
import ru.hitsbank.bank_common.data.utils.toResult
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.map
import javax.inject.Inject
import javax.inject.Singleton
import ru.hitsbank.bank_common.domain.Result

@Singleton
class ProfileRepository @Inject constructor(
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
                        sessionManager.saveIsUserBlocked(result.data.isBlocked)
                    }
                }
                .map(mapper::map)
        }
    }

    override suspend fun getProfilesPage(
        roleType: RoleType?,
        page: PageInfo,
        query: String?,
    ): Result<List<ProfileEntity>> {
        return apiCall(Dispatchers.IO) {
            profileApi.getProfilesPage(roleType?.name, page.pageNumber, page.pageSize, query)
                .toResult()
                .map { list -> list.map { profile -> mapper.map(profile) } }
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