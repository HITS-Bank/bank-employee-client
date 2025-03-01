package com.hits.bankemployee.login.model

data class LoginScreenModel(
    val email: String,
    val password: String,
) {

    companion object {
        val EMPTY = LoginScreenModel(
            email = "",
            password = "",
        )
    }
}