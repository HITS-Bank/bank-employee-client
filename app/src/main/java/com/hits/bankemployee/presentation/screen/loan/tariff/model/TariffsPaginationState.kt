package com.hits.bankemployee.presentation.screen.loan.tariff.model

import com.hits.bankemployee.presentation.screen.loan.tariff.viewmodel.TariffsScreenViewModel
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.PaginationStateHolder

data class TariffsPaginationState(
    override val paginationState: PaginationState,
    override val data: List<TariffModel>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val queryDisplayText: String,
    val query: String,
    val sortingProperty: SortingProperty,
    val sortingOrder: SortingOrder,
    val isSortingPropertyMenuOpen: Boolean,
    val isSortingOrderMenuOpen: Boolean,
    val dialogState: TariffsScreenDialogState,
    val isPerformingAction: Boolean,
) : PaginationStateHolder<TariffModel> {
    override fun copyWith(
        paginationState: PaginationState,
        data: List<TariffModel>,
        pageNumber: Int
    ): PaginationStateHolder<TariffModel> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<TariffModel> {
        return copy(data = emptyList(), pageNumber = 1)
    }

    companion object {
        val EMPTY = TariffsPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = TariffsScreenViewModel.PAGE_SIZE,
            queryDisplayText = "",
            query = "",
            sortingProperty = SortingProperty.NAME,
            sortingOrder = SortingOrder.DESCENDING,
            isSortingPropertyMenuOpen = false,
            isSortingOrderMenuOpen = false,
            dialogState = TariffsScreenDialogState.None,
            isPerformingAction = false,
        )
    }
}
