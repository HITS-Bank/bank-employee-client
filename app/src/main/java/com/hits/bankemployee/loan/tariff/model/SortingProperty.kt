package com.hits.bankemployee.loan.tariff.model

import com.hits.bankemployee.core.domain.entity.loan.LoanTariffSortingProperty
import com.hits.bankemployee.core.presentation.common.component.dropdown.DropdownItem

enum class SortingProperty(override val title: String) : DropdownItem {
    NAME("Названию"),
    INTEREST_RATE("Процентной ставке"),
    CREATED_AT("Времени создания"),
}

fun SortingProperty.toDomain(): LoanTariffSortingProperty {
    return when (this) {
        SortingProperty.NAME -> LoanTariffSortingProperty.NAME
        SortingProperty.INTEREST_RATE -> LoanTariffSortingProperty.INTEREST_RATE
        SortingProperty.CREATED_AT -> LoanTariffSortingProperty.CREATED_AT
    }
}