package com.hits.bankemployee.presentation.screen.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.interactor.UserInteractor
import com.hits.bankemployee.domain.interactor.ValidationInteractor
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEffect
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEvent
import com.hits.bankemployee.presentation.screen.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.presentation.screen.users.model.CreateUserDialogModel
import com.hits.bankemployee.presentation.screen.users.model.CreateUserDialogState
import com.hits.bankemployee.presentation.screen.users.model.UsersScreenModel
import com.hits.bankemployee.presentation.screen.users.model.getIfShown
import com.hits.bankemployee.presentation.screen.users.model.updateIfShown
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.dropFirstBlank
import javax.inject.Inject

@HiltViewModel
class UsersScreenViewModel @Inject constructor(
    private val validationInteractor: ValidationInteractor,
    private val userInteractor: UserInteractor,
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
            UsersScreenEvent.CreateUser -> {
                if (state.value.isCreatingUser) return

                viewModelScope.launch {
                    val userModel = state.value.createUserDialogState.getIfShown()
                    if (userModel == null) {
                        _effects.emit(UsersScreenEffect.ShowUserCreationError)
                        return@launch
                    }
                    val request = mapper.map(userModel)
                    userInteractor.registerUser(request).collectLatest { state ->
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
            is UsersScreenEvent.CreateUserRolesChanged -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            roles = event.roles,
                        )
                    },
                )
            }
            is UsersScreenEvent.CreateUserRolesDropdownExpanded -> _state.update { state ->
                state.copy(
                    createUserDialogState = state.createUserDialogState.updateIfShown { model ->
                        model.copy(
                            isRolesDropdownExpanded = event.isExpanded,
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