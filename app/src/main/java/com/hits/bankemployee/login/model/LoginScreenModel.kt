package com.hits.bankemployee.login.model

data class LoginScreenModel(
    val email: String,
    val password: String,
    val isLoading: Boolean,
) {

    companion object {
        val EMPTY = LoginScreenModel(
            email = "",
            password = "",
            isLoading = false,
        )
    }
}