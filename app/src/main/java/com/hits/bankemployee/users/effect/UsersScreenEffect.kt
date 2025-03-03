package com.hits.bankemployee.users.effect

sealed interface UsersScreenEffect {

    data class ReloadUsers(val query: String) : UsersScreenEffect

    data object ShowUserCreationError : UsersScreenEffect
}