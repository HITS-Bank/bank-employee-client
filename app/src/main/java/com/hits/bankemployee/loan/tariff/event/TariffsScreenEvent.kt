package com.hits.bankemployee.loan.tariff.event

import com.hits.bankemployee.loan.tariff.model.SortingOrder
import com.hits.bankemployee.loan.tariff.model.SortingProperty

sealed interface TariffsScreenEvent {

    data class QueryChanged(
        val query: String,
    ) : TariffsScreenEvent

    data class SortingOrderChanged(
        val sortingOrder: SortingOrder
    ) : TariffsScreenEvent

    data object OpenSortingOrderMenu : TariffsScreenEvent

    data object CloseSortingOrderMenu : TariffsScreenEvent

    data class SortingPropertyChanged(
        val sortingProperty: SortingProperty
    ) : TariffsScreenEvent

    data object OpenSortingPropertyMenu : TariffsScreenEvent

    data object CloseSortingPropertyMenu : TariffsScreenEvent

    data class TariffDeleteClicked(
        val tariffId: String,
        val tariffName: String,
    ) : TariffsScreenEvent

    data object TariffDeleteConfirmed : TariffsScreenEvent

    data object TariffCreateClicked : TariffsScreenEvent

    data class TariffCreateNameChanged(
        val name: String,
    ) : TariffsScreenEvent

    data class TariffCreateInterestRateChanged(
        val interestRate: String,
    ) : TariffsScreenEvent

    data object TariffCreateConfirmed : TariffsScreenEvent

    data object TariffDialogClosed : TariffsScreenEvent
}