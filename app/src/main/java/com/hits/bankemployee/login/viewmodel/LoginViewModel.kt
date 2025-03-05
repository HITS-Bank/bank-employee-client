package com.hits.bankemployee.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.common.Constants.EMPLOYEE_APP_CHANNEL
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.interactor.AuthInteractor
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.navigation.BottomBarRoot
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.navigation.base.replace
import com.hits.bankemployee.login.event.LoginEffect
import com.hits.bankemployee.login.event.LoginEvent
import com.hits.bankemployee.login.mapper.LoginScreenModelMapper
import com.hits.bankemployee.login.model.LoginScreenModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authInteractor: AuthInteractor,
    private val mapper: LoginScreenModelMapper,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state: MutableStateFlow<BankUiState<LoginScreenModel>> =
        MutableStateFlow(BankUiState.Ready(LoginScreenModel.EMPTY))
    val uiState = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>()
    val effects = _effects.receiveAsFlow()

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
            authInteractor
                .login(
                    channel = EMPLOYEE_APP_CHANNEL,
                    request = mapper.map(request),
                ).collectLatest { state ->
                    when (state) {
                        State.Loading -> {
                            _state.updateIfSuccess { it.copy(isLoading = true) }
                        }

                        is State.Error -> {
                            sendEffect(LoginEffect.OnError)
                            _state.updateIfSuccess { it.copy(isLoading = false) }
                        }

                        is State.Success -> {
                            authInteractor.getIsUserBlocked().collectLatest { blockedState ->
                                when (blockedState) {
                                    is State.Error -> {
                                        sendEffect(LoginEffect.OnError)
                                        _state.updateIfSuccess { it.copy(isLoading = false) }
                                    }
                                    State.Loading -> Unit
                                    is State.Success -> {
                                        if (blockedState.data) {
                                            sendEffect(LoginEffect.OnBlocked)
                                        } else {
                                            navigationManager.replace(BottomBarRoot.destination)
                                        }
                                        _state.updateIfSuccess { it.copy(isLoading = false) }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        _effects.trySend(effect)
    }
}