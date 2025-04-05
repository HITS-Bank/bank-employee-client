package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.UserApi
import com.hits.bankemployee.data.mapper.UserMapper
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.UserEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toCompletableResult
import ru.hitsbank.bank_common.data.utils.toResult
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.map
import javax.inject.Inject
import javax.inject.Singleton
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.entity.RoleType

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapper,
) : IUserRepository {

    override suspend fun getProfilesPage(
        roleType: RoleType,
        page: PageInfo,
        query: String?,
    ): Result<List<UserEntity>> {
        return apiCall(Dispatchers.IO) {
            userApi.getProfilesPage(roleType.name, page.pageNumber, page.pageSize, query)
                .toResult()
                .map { list -> list.map { profile -> mapper.map(profile) } }
        }
    }

    override suspend fun banUser(userId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            userApi.banUser(userId)
                .toCompletableResult()
        }
    }

    override suspend fun unbanUser(userId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            userApi.unbanUser(userId)
                .toCompletableResult()
        }
    }

    override suspend fun registerUser(request: RegisterRequestEntity): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            userApi.registerUser(mapper.map(request))
                .toCompletableResult()
        }
    }
}