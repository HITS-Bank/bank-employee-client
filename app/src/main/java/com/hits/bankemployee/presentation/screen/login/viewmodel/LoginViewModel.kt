package com.hits.bankemployee.presentation.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.common.Constants.EMPLOYEE_APP_CHANNEL
import com.hits.bankemployee.domain.interactor.AuthInteractor
import com.hits.bankemployee.presentation.navigation.BottomBarRoot
import com.hits.bankemployee.presentation.screen.login.event.LoginEffect
import com.hits.bankemployee.presentation.screen.login.event.LoginEvent
import com.hits.bankemployee.presentation.screen.login.mapper.LoginScreenModelMapper
import com.hits.bankemployee.presentation.screen.login.model.LoginScreenModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.replace
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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