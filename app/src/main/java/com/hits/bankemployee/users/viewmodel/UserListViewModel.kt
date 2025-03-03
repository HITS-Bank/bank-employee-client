package com.hits.bankemployee.users.viewmodel

import com.hits.bankemployee.core.domain.common.State
import com.hits.bankemployee.core.presentation.common.BankUiState
import com.hits.bankemployee.core.presentation.pagination.PaginationEvent
import com.hits.bankemployee.core.presentation.pagination.PaginationViewModel
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.model.userlist.UserListPaginationState
import com.hits.bankemployee.users.model.userlist.UserModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class UserListViewModel(
    private val role: UserRole = UserRole.CLIENT,
) : PaginationViewModel<UserModel>(BankUiState.Ready(UserListPaginationState.EMPTY)) {

    init {
        onPaginationEvent(PaginationEvent.Reload)
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<UserModel>>> {
        //TODO actual logic
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