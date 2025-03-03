package com.hits.bankemployee.users.effect

sealed interface UserListEffect {

    data object ShowBlockError : UserListEffect

    data object ShowUnblockError : UserListEffect
}