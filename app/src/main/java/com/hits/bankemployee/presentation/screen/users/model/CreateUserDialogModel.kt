package com.hits.bankemployee.presentation.screen.users.model

import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownItem

sealed interface CreateUserDialogState {

    data object Hidden : CreateUserDialogState

    data class Shown(val model: CreateUserDialogModel) : CreateUserDialogState
}

fun CreateUserDialogState.updateIfShown(block: (CreateUserDialogModel) -> CreateUserDialogModel): CreateUserDialogState {
    return when (this) {
        is CreateUserDialogState.Hidden -> this
        is CreateUserDialogState.Shown -> CreateUserDialogState.Shown(block(model))
    }
}

fun CreateUserDialogState.getIfShown(): CreateUserDialogModel? {
    return (this as? CreateUserDialogState.Shown)?.model
}

data class CreateUserDialogModel(
    val firstName: String,
    val isFirstNameValid: Boolean,
    val lastName: String,
    val isLastNameValid: Boolean,
    val password: String,
    val isPasswordValid: Boolean,
    val roles: RoleTypeCombination,
    val isRolesDropdownExpanded: Boolean,
) {
    val createButtonEnabled: Boolean
        get() = isFirstNameValid && isLastNameValid && isPasswordValid && firstName.isNotBlank() && lastName.isNotBlank() && password.isNotBlank()

    companion object {
        val EMPTY = CreateUserDialogModel(
            firstName = "",
            isFirstNameValid = true,
            lastName = "",
            isLastNameValid = true,
            password = "",
            isPasswordValid = true,
            roles = RoleTypeCombination.CLIENT,
            isRolesDropdownExpanded = false,
        )
    }
}

enum class RoleTypeCombination(override val title: String): DropdownItem {
    CLIENT("Клиент"),
    EMPLOYEE("Сотрудник"),
    CLIENT_EMPLOYEE("Клиент и сотрудник"),
}

fun RoleTypeCombination.toRoleTypes(): List<RoleType> {
    return when (this) {
        RoleTypeCombination.CLIENT -> listOf(RoleType.CLIENT)
        RoleTypeCombination.EMPLOYEE -> listOf(RoleType.EMPLOYEE)
        RoleTypeCombination.CLIENT_EMPLOYEE -> listOf(RoleType.CLIENT, RoleType.EMPLOYEE)
    }
}