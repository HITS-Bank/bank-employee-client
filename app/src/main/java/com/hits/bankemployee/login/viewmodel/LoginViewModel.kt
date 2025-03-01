package com.hits.bankemployee.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.common.Constants.EMPLOYEE_APP_CHANNEL
import com.hits.bankemployee.core.domain.interactor.AuthInteractor
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.login.event.LoginEvent
import com.hits.bankemployee.login.mapper.LoginScreenModelMapper
import com.hits.bankemployee.login.model.LoginScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authInteractor: AuthInteractor,
    private val mapper: LoginScreenModelMapper,
) : ViewModel() {

    private val _state: MutableStateFlow<BankUiState<LoginScreenModel>> =
        MutableStateFlow(BankUiState.Ready(LoginScreenModel.EMPTY))
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> onEmailChanged(event.email)
            is LoginEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.LogIn -> logIn()
        }
    }

    private fun onEmailChanged(email: String) {
        _state.updateIfSuccess { it.copy(email = email) }
    }

    private fun onPasswordChanged(password: String) {
        _state.updateIfSuccess { it.copy(password = password) }
    }

    private fun logIn() {
        val request = _state.getIfSuccess() ?: return
        viewModelScope.launch {
            authInteractor.login(
                channel = EMPLOYEE_APP_CHANNEL,
                request = mapper.map(request),
            )
        }
    }
}