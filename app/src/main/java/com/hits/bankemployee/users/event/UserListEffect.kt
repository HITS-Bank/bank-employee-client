package com.hits.bankemployee.users.event

sealed interface UserListEffect {

    data object ShowBlockError : UserListEffect

    data object ShowUnblockError : UserListEffect
}