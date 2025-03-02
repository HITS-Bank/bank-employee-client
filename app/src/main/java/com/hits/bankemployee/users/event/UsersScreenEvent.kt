package com.hits.bankemployee.users.event

import com.hits.bankemployee.users.model.UsersTab

sealed interface UsersScreenEvent {

    class QueryChanged(val query: String) : UsersScreenEvent

    class TabSelected(val tab: UsersTab) : UsersScreenEvent

    class CreateUserFirstNameChanged(val firstName: String) : UsersScreenEvent

    class CreateUserLastNameChanged(val lastName: String) : UsersScreenEvent

    class CreateUserEmailChanged(val email: String) : UsersScreenEvent

    class CreateUserPasswordChanged(val password: String) : UsersScreenEvent

    object CreateUserDialogOpen : UsersScreenEvent

    object CreateUserDialogClose : UsersScreenEvent

    object CreateUser : UsersScreenEvent
}