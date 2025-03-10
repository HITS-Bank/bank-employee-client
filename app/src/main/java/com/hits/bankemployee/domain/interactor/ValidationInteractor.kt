package com.hits.bankemployee.domain.interactor

import java.util.regex.Pattern

class ValidationInteractor(private val emailPattern: Pattern) {

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && emailPattern.matcher(email).matches()
    }
}