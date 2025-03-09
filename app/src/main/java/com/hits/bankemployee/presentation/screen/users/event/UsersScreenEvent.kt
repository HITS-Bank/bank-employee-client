package com.hits.bankemployee.presentation.screen.users.event

import com.hits.bankemployee.presentation.screen.users.model.UsersTab

sealed interface UsersScreenEvent {

    data class QueryChanged(val query: String) : UsersScreenEvent

    data class TabSelected(val tab: UsersTab) : UsersScreenEvent

    data class CreateUserFirstNameChanged(val firstName: String) : UsersScreenEvent

    data class CreateUserLastNameChanged(val lastName: String) : UsersScreenEvent

    data class CreateUserEmailChanged(val email: String) : UsersScreenEvent

    data class CreateUserPasswordChanged(val password: String) : UsersScreenEvent

    data object CreateUserDialogOpen : UsersScreenEvent

    data object CreateUserDialogClose : UsersScreenEvent

    data object CreateUser : UsersScreenEvent
}