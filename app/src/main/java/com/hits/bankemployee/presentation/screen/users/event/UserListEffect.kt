package com.hits.bankemployee.presentation.screen.users.event

sealed interface UserListEffect {

    data object ShowBlockError : UserListEffect

    data object ShowUnblockError : UserListEffect
}