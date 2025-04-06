package com.hits.bankemployee.presentation.screen.users.model

import ru.hitsbank.bank_common.domain.entity.RoleType

enum class UserRole(val title: String) {
    CLIENT("Клиент"),
    EMPLOYEE("Сотрудник"),
}

fun RoleType.toUserRole(): UserRole = when (this) {
    RoleType.CLIENT -> UserRole.CLIENT
    RoleType.EMPLOYEE -> UserRole.EMPLOYEE
}

fun UserRole.toRoleType(): RoleType = when (this) {
    UserRole.CLIENT -> RoleType.CLIENT
    UserRole.EMPLOYEE -> RoleType.EMPLOYEE
}

enum class UsersTab(val title: String, val role: UserRole) {
    CLIENTS("Клиенты", UserRole.CLIENT),
    EMPLOYEES("Сотрудники", UserRole.EMPLOYEE),
}