package com.hits.bankemployee.users.model

enum class UserRole(val title: String) {
    CLIENT("Клиент"),
    EMPLOYEE("Сотрудник"),
}

enum class UsersTab(val title: String, val creationTitle: String, val role: UserRole) {
    CLIENTS("Клиенты", "клиента", UserRole.CLIENT),
    EMPLOYEES("Сотрудники", "сотрудника", UserRole.EMPLOYEE),
}