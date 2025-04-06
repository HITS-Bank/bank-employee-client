package com.hits.bankemployee.presentation.screen.login.event

sealed interface LoginEffect {

    data object OnError : LoginEffect

    data object OnBlocked : LoginEffect

    data class OpenAuthPage(val uri: String) : LoginEffect
}