package com.hits.bankemployee.data.repository

import com.hits.bankemployee.data.api.UserApi
import com.hits.bankemployee.data.mapper.UserMapper
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.entity.UserEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import com.hits.bankemployee.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.model.RequestIdHolder
import ru.hitsbank.bank_common.data.model.getNewRequestId
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

    private var banUserIdHolder: RequestIdHolder? = null
    private var unbanUserIdHolder: RequestIdHolder? = null
    private var registerUserIdHolder: RequestIdHolder? = null

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
            val idHolder = banUserIdHolder.getNewRequestId(userId.hashCode())
            banUserIdHolder = idHolder
            userApi.banUser(userId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        banUserIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun unbanUser(userId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            val idHolder = unbanUserIdHolder.getNewRequestId(userId.hashCode())
            unbanUserIdHolder = idHolder
            userApi.unbanUser(userId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        unbanUserIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun registerUser(request: RegisterRequestEntity): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            val idHolder = registerUserIdHolder.getNewRequestId(request.hashCode())
            registerUserIdHolder = idHolder
            userApi.registerUser(mapper.map(idHolder.requestId, request))
                .also { response ->
                    if (response.isSuccessful) {
                        registerUserIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }
}