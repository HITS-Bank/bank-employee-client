package com.hits.bankemployee.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.common.dropFirstBlank
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.interactor.ProfileInteractor
import com.hits.bankemployee.core.domain.interactor.ValidationInteractor
import com.hits.bankemployee.users.event.UsersScreenEffect
import com.hits.bankemployee.users.event.UsersScreenEvent
import com.hits.bankemployee.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.users.model.CreateUserDialogModel
import com.hits.bankemployee.users.model.CreateUserDialogState
import com.hits.bankemployee.users.model.UsersScreenModel
import com.hits.bankemployee.users.model.getIfShown
import com.hits.bankemployee.users.model.updateIfShown
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersScreenViewModel(
    private val validationInteractor: ValidationInteractor,
    private val profileInteractor: ProfileInteractor,
    private val mapper: UsersScreenModelMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(UsersScreenModel.EMPTY)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<UsersScreenEffect>()
    val effects = _effects.asSharedFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        subscribeToQueryFlow()
    }

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.QueryChanged -> {
                _state.update { state -> state.copy(query = event.query) }
                queryFlow.value = event.query
            }
            is UsersScreenEvent.TabSelected -> {
                _state.update { state -> state.copy(selectedTab = event.tab) }
            }
            UsersScreenEvent.CreateUser -> {
                if (state.value.isCreatingUser) return

                viewModelScope.launch {
                    val userModel = state.value.createUserDialogState.getIfShown()
                    if (userModel == null) {
                        _effects.emit(UsersScreenEffect.ShowUserCreationError)
                        return@launch
                    }
                    val request = mapper.map(userModel, state.value.selectedTab.role)
                    profileInteractor.registerUser(request).collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.update { oldState ->
                                    oldState.copy(isCreatingUser = true)
                                }
                            }
                            is State.Error -> {
                                _state.update { oldState ->
                                    oldState.copy(isCreatingUser = false)
                                }
                                _effects.emit(UsersScreenEffect.ShowUserCreationError)
                            }
                            is State.Success -> {
                                _state.update { oldState ->
                                    oldState.copy(
                                        createUserDialogState = CreateUserDialogState.Hidden,
                                        isCreatingUser = false,
                                    )
                                }
                                _effects.emit(UsersScreenEffect.ReloadUsers(queryFlow.value))
                            }
                        }
                    }
                }
            }
            UsersScreenEvent.CreateUserDialogClose -> {
                if (state.value.isCreatingUser) return

                _state.update { state ->
                    state.copy(
                        createUserDialogState = CreateUserDialogState.Hidden,
                    )
                }
            }
            UsersScreenEvent.CreateUserDialogOpen -> {
                if (state.value.isCreatingUser) return

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

    @OptIn(FlowPreview::class)
    private fun subscribeToQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(1000)
                .distinctUntilChanged()
                .dropFirstBlank()
                .collectLatest { query ->
                    _effects.emit(UsersScreenEffect.ReloadUsers(query))
                }
        }
    }
}