package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.LoginRequest
import com.hits.bankemployee.domain.entity.LoginRequestEntity

class AuthMapper {

    fun map(entity: LoginRequestEntity): LoginRequest {
        return LoginRequest(
            email = entity.email,
            password = entity.password,
        )
    }
}