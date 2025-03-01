package com.hits.bankemployee.login.mapper

import com.hits.bankemployee.core.domain.entity.LoginRequestEntity
import com.hits.bankemployee.login.model.LoginScreenModel

class LoginScreenModelMapper {

    fun map(state: LoginScreenModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = state.email,
            password = state.password,
        )
    }
}