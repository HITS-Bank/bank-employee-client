package com.hits.bankemployee.users.model.userlist

import com.hits.bankemployee.core.presentation.pagination.PaginationState
import com.hits.bankemployee.core.presentation.pagination.PaginationStateHolder

data class UserListPaginationState(
    override val paginationState: PaginationState,
    override val data: List<UserModel>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val blockUserId: String?,
    val unblockUserId: String?,
    val isPerformingAction: Boolean,
    val query: String,
) : PaginationStateHolder<UserModel> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<UserModel>,
        pageNumber: Int
    ): PaginationStateHolder<UserModel> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<UserModel> {
        return copy(data = emptyList(), pageNumber = 0)
    }

    companion object {
        val EMPTY = UserListPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 0,
            pageSize = 5,
            blockUserId = null,
            unblockUserId = null,
            isPerformingAction = false,
            query = "",
        )
    }
}
