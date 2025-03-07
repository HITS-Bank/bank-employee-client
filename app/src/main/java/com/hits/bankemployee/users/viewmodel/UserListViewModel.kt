package com.hits.bankemployee.users.viewmodel

import androidx.lifecycle.viewModelScope
import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.domain.common.map
import com.hits.bankemployee.core.domain.entity.PageInfo
import com.hits.bankemployee.core.domain.interactor.ProfileInteractor
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.common.getIfSuccess
import com.hits.bankemployee.core.presentation.common.updateIfSuccess
import com.hits.bankemployee.core.presentation.navigation.UserDetails
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.navigation.base.forwardWithCallbackResult
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import com.hits.bankemployee.users.event.UserListEffect
import com.hits.bankemployee.users.event.UserListEvent
import com.hits.bankemployee.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.model.toRoleType
import com.hits.bankemployee.users.model.userlist.UserListPaginationState
import com.hits.bankemployee.users.model.userlist.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserListViewModel(
    private val role: UserRole = UserRole.CLIENT,
    private val navigationManager: NavigationManager,
    private val profileInteractor: ProfileInteractor,
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
                if (role != UserRole.CLIENT) return
                navigationManager.forwardWithCallbackResult(
                    UserDetails.withArgs(
                        event.userId,
                        event.fullName,
                        event.isBlocked,
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
                    profileInteractor.banUser(userId).collectLatest { state ->
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
                    profileInteractor.unbanUser(userId).collectLatest { state ->
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
        return profileInteractor.getProfilesPage(
            roleType = role.toRoleType(),
            page = pageInfo,
            query = state.value.getIfSuccess()?.query?.takeIf { it.isNotBlank() },
        ).map { state -> state.map(mapper::map) }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}