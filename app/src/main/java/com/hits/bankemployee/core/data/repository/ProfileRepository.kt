package com.hits.bankemployee.core.data.repository

import com.hits.bankemployee.core.data.api.ProfileApi
import com.hits.bankemployee.core.data.common.apiCall
import com.hits.bankemployee.core.data.common.toResult
import com.hits.bankemployee.core.data.datasource.SessionManager
import com.hits.bankemployee.core.data.mapper.ProfileMapper
import com.hits.bankemployee.core.domain.common.Result
import com.hits.bankemployee.core.domain.common.map
import com.hits.bankemployee.core.domain.entity.ProfileEntity
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
}