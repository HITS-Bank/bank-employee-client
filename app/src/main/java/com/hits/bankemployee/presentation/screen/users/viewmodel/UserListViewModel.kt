package com.hits.bankemployee.presentation.screen.users.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.domain.entity.PageInfo
import com.hits.bankemployee.domain.interactor.UserInteractor
import com.hits.bankemployee.presentation.navigation.UserDetails
import com.hits.bankemployee.presentation.screen.users.event.UserListEffect
import com.hits.bankemployee.presentation.screen.users.event.UserListEvent
import com.hits.bankemployee.presentation.screen.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.presentation.screen.users.model.UserRole
import com.hits.bankemployee.presentation.screen.users.model.toRoleType
import com.hits.bankemployee.presentation.screen.users.model.userlist.UserListPaginationState
import com.hits.bankemployee.presentation.screen.users.model.userlist.UserModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.updateIfSuccess
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationViewModel

@HiltViewModel(assistedFactory = UserListViewModel.Factory::class)
class UserListViewModel @AssistedInject constructor(
    @Assisted private val role: UserRole,
    private val navigationManager: NavigationManager,
    private val userInteractor: UserInteractor,
    private val mapper: UsersScreenModelMapper,
) : PaginationViewModel<UserModel, UserListPaginationState>(
    BankUiState.Ready(
        UserListPaginationState.EMPTY
    )
) {

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
                navigationManager.forwardWithCallbackResult(
                    UserDetails.withArgs(
                        event.userId,
                        event.fullName,
                        event.isBlocked,
                        event.roles,
                    )
                ) {
                    onPaginationEvent(PaginationEvent.Reload)
                }
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
                viewModelScope.launch {
                    val userId = state.value.getIfSuccess()?.blockUserId
                    if (userId == null) {
                        _state.updateIfSuccess { state ->
                            state.copy(blockUserId = null)
                        }
                        _effects.emit(UserListEffect.ShowBlockError)
                        return@launch
                    }
                    userInteractor.banUser(userId).collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = true)
                                }
                            }

                            is State.Error -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = false, blockUserId = null)
                                }
                                _effects.emit(UserListEffect.ShowBlockError)
                            }

                            is State.Success -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = false, blockUserId = null)
                                }
                                onPaginationEvent(PaginationEvent.Reload)
                            }
                        }
                    }
                }
            }

            UserListEvent.ConfirmUnblock -> {
                if (state.value.getIfSuccess()?.isPerformingAction == true) return
                viewModelScope.launch {
                    val userId = state.value.getIfSuccess()?.unblockUserId
                    if (userId == null) {
                        _state.updateIfSuccess { state ->
                            state.copy(unblockUserId = null)
                        }
                        _effects.emit(UserListEffect.ShowUnblockError)
                        return@launch
                    }
                    userInteractor.unbanUser(userId).collectLatest { state ->
                        when (state) {
                            State.Loading -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = true)
                                }
                            }

                            is State.Error -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = false, unblockUserId = null)
                                }
                                _effects.emit(UserListEffect.ShowUnblockError)
                            }

                            is State.Success -> {
                                _state.updateIfSuccess { oldState ->
                                    oldState.copy(isPerformingAction = false, unblockUserId = null)
                                }
                                onPaginationEvent(PaginationEvent.Reload)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getNextPageContents(pageNumber: Int): Flow<State<List<UserModel>>> {
        val pageInfo = PageInfo(
            pageNumber = pageNumber,
            pageSize = state.value.getIfSuccess()?.pageSize ?: PAGE_SIZE,
        )
        return userInteractor.getProfilesPage(
            roleType = role.toRoleType(),
            page = pageInfo,
            query = state.value.getIfSuccess()?.query?.takeIf { it.isNotBlank() },
        ).map { state -> state.map(mapper::map) }
    }

    companion object {
        const val PAGE_SIZE = 10
    }

    @AssistedFactory
    interface Factory {
        fun create(role: UserRole): UserListViewModel
    }
}