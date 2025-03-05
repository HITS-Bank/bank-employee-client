package com.hits.bankemployee.users.event

sealed interface UsersScreenEffect {

    data class ReloadUsers(val query: String) : UsersScreenEffect

    data object ShowUserCreationError : UsersScreenEffect
}