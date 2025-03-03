package com.hits.bankemployee.users.event

sealed interface UserListEvent {

    data class BlockUser(val userId: String) : UserListEvent

    data class UnblockUser(val userId: String) : UserListEvent

    data class Reload(val query: String) : UserListEvent

    data class OpenClientDetails(val userId: String) : UserListEvent

    object CloseBlockDialog : UserListEvent

    object ConfirmBlock : UserListEvent

    object ConfirmUnblock : UserListEvent
}