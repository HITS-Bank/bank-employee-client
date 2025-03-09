package com.hits.bankemployee.presentation.screen.users.model

data class UsersScreenModel(
    val query: String,
    val selectedTab: UsersTab,
    val createUserDialogState: CreateUserDialogState,
    val isCreatingUser: Boolean,
) {

    companion object {
        val EMPTY = UsersScreenModel(
            query = "",
            selectedTab = UsersTab.CLIENTS,
            createUserDialogState = CreateUserDialogState.Hidden,
            isCreatingUser = false,
        )
    }
}
