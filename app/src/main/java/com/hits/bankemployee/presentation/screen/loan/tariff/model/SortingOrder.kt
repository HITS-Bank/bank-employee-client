package com.hits.bankemployee.presentation.screen.loan.tariff.model

import com.hits.bankemployee.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.presentation.common.component.dropdown.DropdownItem

enum class SortingOrder(override val title: String) : DropdownItem {
    ASCENDING("По возрастанию"),
    DESCENDING("По убыванию"),
}

fun SortingOrder.toDomain(): LoanTariffSortingOrder {
    return when (this) {
        SortingOrder.ASCENDING -> LoanTariffSortingOrder.ASCENDING
        SortingOrder.DESCENDING -> LoanTariffSortingOrder.DESCENDING
    }
}