package com.hits.bankemployee.core.domain.interactor

import java.util.regex.Pattern

class ValidationInteractor(private val emailPattern: Pattern) {

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank() && password.length >= 8
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && emailPattern.matcher(email).matches()
    }
}