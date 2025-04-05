package com.hits.bankemployee.presentation.screen.users.event

import com.hits.bankemployee.presentation.screen.users.model.RoleTypeCombination

sealed interface UsersScreenEvent {

    data class QueryChanged(val query: String) : UsersScreenEvent

    data class CreateUserFirstNameChanged(val firstName: String) : UsersScreenEvent

    data class CreateUserLastNameChanged(val lastName: String) : UsersScreenEvent

    data class CreateUserPasswordChanged(val password: String) : UsersScreenEvent

    data class CreateUserRolesChanged(val roles: RoleTypeCombination) : UsersScreenEvent

    data class CreateUserRolesDropdownExpanded(val isExpanded: Boolean) : UsersScreenEvent

    data object CreateUserDialogOpen : UsersScreenEvent

    data object CreateUserDialogClose : UsersScreenEvent

    data object CreateUser : UsersScreenEvent
}