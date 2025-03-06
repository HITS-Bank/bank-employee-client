package com.hits.bankemployee.loan.tariff.model

import com.hits.bankemployee.core.presentation.common.component.dropdown.DropdownItem

enum class SortingProperty(override val title: String) : DropdownItem {
    NAME("Названию"),
    INTEREST_RATE("Процентной ставке"),
    CREATED_AT("Времени создания"),
}