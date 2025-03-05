package com.hits.bankemployee.users.model

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
    val email: String,
    val isEmailValid: Boolean,
    val password: String,
    val isPasswordValid: Boolean,
) {
    val createButtonEnabled: Boolean
        get() = isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank()

    companion object {
        val EMPTY = CreateUserDialogModel(
            firstName = "",
            isFirstNameValid = true,
            lastName = "",
            isLastNameValid = true,
            email = "",
            isEmailValid = true,
            password = "",
            isPasswordValid = true,
        )
    }
}