package com.hits.bankemployee.loan.tariff.model

import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingOrder
import com.hits.bankemployee.core.presentation.common.component.dropdown.DropdownItem

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