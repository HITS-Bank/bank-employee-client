package com.hits.bankemployee.login.event

sealed interface LoginEffect {

    data object OnError : LoginEffect

    data object OnBlocked : LoginEffect
}