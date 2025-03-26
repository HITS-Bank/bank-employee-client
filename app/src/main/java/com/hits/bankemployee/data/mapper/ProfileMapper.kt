package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.ProfileResponse
import com.hits.bankemployee.data.model.RegisterRequest
import com.hits.bankemployee.domain.entity.ProfileEntity
import com.hits.bankemployee.domain.entity.RegisterRequestEntity

class ProfileMapper {

    fun map(response: ProfileResponse): ProfileEntity {
        with (response) {
            return ProfileEntity(
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
                email = email,
                password = password,
                role = role,
            )
        }
    }
}