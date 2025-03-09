package com.hits.bankemployee.presentation.screen.login.mapper

import com.hits.bankemployee.domain.entity.LoginRequestEntity
import com.hits.bankemployee.presentation.screen.login.model.LoginScreenModel

class LoginScreenModelMapper {

    fun map(state: LoginScreenModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = state.email,
            password = state.password,
        )
    }
}