package com.hits.bankemployee.login.event

sealed interface LoginEvent {

    data class OnEmailChanged(val email: String) : LoginEvent

    data class OnPasswordChanged(val password: String) : LoginEvent

    data object LogIn : LoginEvent
}