package com.hits.bankemployee.core.data.mapper

import com.hits.bankemployee.core.data.model.ProfileResponse
import com.hits.bankemployee.core.domain.entity.ProfileEntity

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
}