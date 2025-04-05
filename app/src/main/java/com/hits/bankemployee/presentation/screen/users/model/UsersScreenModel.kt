package com.hits.bankemployee.presentation.screen.users.model

data class UsersScreenModel(
    val query: String,
    val createUserDialogState: CreateUserDialogState,
    val isCreatingUser: Boolean,
) {

    companion object {
        val EMPTY = UsersScreenModel(
            query = "",
            createUserDialogState = CreateUserDialogState.Hidden,
            isCreatingUser = false,
        )
    }
}
