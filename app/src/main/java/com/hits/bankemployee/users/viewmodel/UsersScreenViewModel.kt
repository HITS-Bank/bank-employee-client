package com.hits.bankemployee.users.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.domain.interactor.ValidationInteractor
import com.hits.bankemployee.users.event.UsersScreenEvent
import com.hits.bankemployee.users.model.CreateUserDialogModel
import com.hits.bankemployee.users.model.CreateUserDialogState
import com.hits.bankemployee.users.model.UsersScreenModel
import com.hits.bankemployee.users.model.updateIfShown
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersScreenViewModel(
    //TODO DI
    private val validationInteractor: ValidationInteractor = ValidationInteractor(
        emailPattern = Patterns.EMAIL_ADDRESS
    )
) : ViewModel() {

    private val _state = MutableStateFlow(UsersScreenModel.EMPTY)
    val state = _state.asStateFlow()

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.QueryChanged -> {
                _state.update { state -> state.copy(query = event.query) }
            }
            is UsersScreenEvent.TabSelected -> {
                _state.update { state -> state.copy(selectedTab = event.tab) }
            }
            UsersScreenEvent.CreateUser -> {
                _state.update { state -> state.copy(isCreatingUser = true) }
                viewModelScope.launch {
                    //TODO change to actual logic
                    delay(2000)
                    _state.update { state ->
                        state.copy(
                            createUserDialogState = CreateUserDialogState.Hidden,
                            isCreatingUser = false,
                        )
                    }
                }
            }
            UsersScreenEvent.CreateUserDialogClose -> {
                _state.update { state ->
                    state.copy(
                        createUserDialogState = CreateUserDialogState.Hidden,
                    )
                }
            }
            UsersScreenEvent.CreateUserDialogOpen -> {
                _state.update { state ->
                    state.copy(
                        createUserDialogState = CreateUserDialogState.Shown(CreateUserDialogModel.EMPTY),
                    )
                }
            }
            is UsersScreenEvent.CreateUserEmailChanged -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            email = event.email,
                            isEmailValid = validationInteractor.isEmailValid(event.email),
                        )
                    },
                )
            }
            is UsersScreenEvent.CreateUserFirstNameChanged -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            firstName = event.firstName,
                            isFirstNameValid = validationInteractor.isNameValid(event.firstName),
                        )
                    },
                )
            }
            is UsersScreenEvent.CreateUserLastNameChanged -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            lastName = event.lastName,
                            isLastNameValid = validationInteractor.isNameValid(event.lastName),
                        )
                    },
                )
            }
            is UsersScreenEvent.CreateUserPasswordChanged -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            password = event.password,
                            isPasswordValid = validationInteractor.isPasswordValid(event.password),
                        )
                    },
                )
            }
        }
    }
}