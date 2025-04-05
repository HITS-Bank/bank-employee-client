package com.hits.bankemployee.presentation.screen.users.event

import ru.hitsbank.bank_common.domain.entity.RoleType

sealed interface UserListEvent {

    data class BlockUser(val userId: String) : UserListEvent

    data class UnblockUser(val userId: String) : UserListEvent

    data class Reload(val query: String) : UserListEvent

    data class OpenClientDetails(
        val userId: String,
        val fullName: String,
        val isBlocked: Boolean,
        val roles: List<RoleType>,
    ) : UserListEvent

    object CloseBlockDialog : UserListEvent

    object ConfirmBlock : UserListEvent

    object ConfirmUnblock : UserListEvent
}