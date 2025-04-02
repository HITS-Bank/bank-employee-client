package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.LoginRequest
import com.hits.bankemployee.domain.entity.LoginRequestEntity
import javax.inject.Inject

class AuthMapper @Inject constructor() {

    fun map(entity: LoginRequestEntity): LoginRequest {
        return LoginRequest(
            email = entity.email,
            password = entity.password,
        )
    }
}