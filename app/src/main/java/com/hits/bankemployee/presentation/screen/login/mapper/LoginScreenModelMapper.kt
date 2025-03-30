package com.hits.bankemployee.presentation.screen.login.mapper

import com.hits.bankemployee.domain.entity.LoginRequestEntity
import com.hits.bankemployee.presentation.screen.login.model.LoginScreenModel
import javax.inject.Inject

class LoginScreenModelMapper @Inject constructor() {

    fun map(state: LoginScreenModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = state.email,
            password = state.password,
        )
    }
}