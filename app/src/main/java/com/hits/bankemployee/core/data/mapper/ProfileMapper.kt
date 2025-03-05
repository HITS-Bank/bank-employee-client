package com.hits.bankemployee.core.data.mapper

import com.hits.bankemployee.core.data.model.ProfileResponse
import com.hits.bankemployee.core.data.model.RegisterRequest
import com.hits.bankemployee.core.domain.entity.ProfileEntity
import com.hits.bankemployee.core.domain.entity.RegisterRequestEntity

class ProfileMapper {

    fun map(response: ProfileResponse): ProfileEntity {
        with (response) {
            return ProfileEntity(
                id = id,
                firstName = firstName,
                lastName = lastName,
                isBanned = isBanned,
                email = email,
                role = role,
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