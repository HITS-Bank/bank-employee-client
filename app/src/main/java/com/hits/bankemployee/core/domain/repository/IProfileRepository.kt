package com.hits.bankemployee.core.domain.repository

import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.common.Result

interface IProfileRepository {

    suspend fun getSelfProfile(): Result<ProfileEntity>
}