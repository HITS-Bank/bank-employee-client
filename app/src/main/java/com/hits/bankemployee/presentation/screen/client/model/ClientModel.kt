package com.hits.bankemployee.presentation.screen.client.model

import com.hits.bankemployee.domain.entity.RoleType

data class ClientModel(
    val id: String,
    val fullName: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>,
)
