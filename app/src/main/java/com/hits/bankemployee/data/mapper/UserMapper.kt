package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.UserResponse
import com.hits.bankemployee.data.model.RegisterRequest
import com.hits.bankemployee.domain.entity.UserEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun map(response: UserResponse): UserEntity {
        with (response) {
            return UserEntity(
                id = id,
                firstName = firstName,
                lastName = lastName,
                isBanned = isBlocked,
                roles = roles,
            )
        }
    }

    fun map(requestEntity: RegisterRequestEntity): RegisterRequest {
        with (requestEntity) {
            return RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                password = password,
                roles = roles,
            )
        }
    }
}