package com.hits.bankemployee.users.model

import com.hits.bankemployee.core.domain.entity.RoleType

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

enum class UsersTab(val title: String, val creationTitle: String, val role: UserRole) {
    CLIENTS("Клиенты", "клиента", UserRole.CLIENT),
    EMPLOYEES("Сотрудники", "сотрудника", UserRole.EMPLOYEE),
}