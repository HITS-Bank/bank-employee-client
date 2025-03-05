package com.hits.bankemployee.users.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.navigation.UserDetails
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.navigation.base.forward
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import com.hits.bankemployee.users.event.UserListEffect
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.model.userlist.UserListPaginationState
import com.hits.bankemployee.users.model.userlist.UserModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.UUID

class UserListViewModel(
    private val role: UserRole = UserRole.CLIENT,
    private val navigationManager: NavigationManager,
) : PaginationViewModel<UserModel, UserListPaginationState>(BankUiState.Ready(UserListPaginationState.EMPTY)) {

    private val _effects = MutableSharedFlow<UserListEffect>()
    val effects = _effects.asSharedFlow()

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.BlockUser -> _state.updateIfSuccess { state ->
                state.copy(blockUserId = event.userId)
            }
            is UserListEvent.UnblockUser -> _state.updateIfSuccess { state ->
                state.copy(unblockUserId = event.userId)
            }
            is UserListEvent.OpenClientDetails -> {
                if (role != UserRole.CLIENT) return
                navigationManager.forward(UserDetails.destinationWithArgs(event.userId))
            }
            is UserListEvent.Reload -> {
                _state.updateIfSuccess { state -> state.copy(query = event.query) }
                onPaginationEvent(PaginationEvent.Reload)
            }
            UserListEvent.CloseBlockDialog -> {
                if (state.value.getIfSuccess()?.isPerformingAction == true) return
                _state.updateIfSuccess { state ->
                    state.copy(blockUserId = null, unblockUserId = null)
                }
            }
            UserListEvent.ConfirmBlock -> {
                if (state.value.getIfSuccess()?.isPerformingAction == true) return
                _state.updateIfSuccess { state ->
                    state.copy(isPerformingAction = true)
                }
                viewModelScope.launch {
                    //TODO actual logic
                    delay(2000)
                    //Common
                    _state.updateIfSuccess { state ->
                        state.copy(isPerformingAction = false, blockUserId = null)
                    }
                    //Success (additional)
                    onPaginationEvent(PaginationEvent.Reload)
                    //Failure (additional)
                    _effects.emit(UserListEffect.ShowBlockError)
                }
            }
            UserListEvent.ConfirmUnblock -> {
                if (state.value.getIfSuccess()?.isPerformingAction == true) return
                _state.updateIfSuccess { state ->
                    state.copy(isPerformingAction = true)
                }
                viewModelScope.launch {
                    //TODO actual logic
                    delay(2000)
                    //Common
                    _state.updateIfSuccess { state ->
                        state.copy(isPerformingAction = false, unblockUserId = null)
                    }
                    //Success (additional)
                    onPaginationEvent(PaginationEvent.Reload)
                    //Failure (additional)
                    _effects.emit(UserListEffect.ShowUnblockError)
                }
            }
        }
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<UserModel>>> {
        //TODO actual logic (including query filtering)
        return flow {
            emit(State.Loading)
            delay(2000)
            emit(
                State.Success(
                    listOf(
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            firstName = "Иван",
                            lastName = "Иванов",
                            isBlocked = false,
                            role = role,
                        ),
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            firstName = "Иван",
                            lastName = "Иванов",
                            isBlocked = false,
                            role = role,
                        ),
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            firstName = "Иван",
                            lastName = "Иванов",
                            isBlocked = true,
                            role = role,
                        ),
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            firstName = "Иван",
                            lastName = "Иванов",
                            isBlocked = false,
                            role = role,
                        ),
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            firstName = "Иван",
                            lastName = "Иванов",
                            isBlocked = false,
                            role = role,
                        )
                    )
                )
            )
        }
    }
}