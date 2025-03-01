package com.hits.bankemployee.core.data.mapper

import com.hits.bankemployee.core.data.model.LoginRequest
import com.hits.bankemployee.core.domain.entity.LoginRequestEntity

class AuthMapper {

    fun map(entity: LoginRequestEntity): LoginRequest {
        return LoginRequest(
            email = entity.email,
            password = entity.password,
        )
    }
}