package com.hits.bankemployee.presentation.screen.loan.tariff.model

sealed interface TariffsScreenDialogState {

    data object None : TariffsScreenDialogState

    data class DeleteTariff(
        val tariffId: String,
        val tariffName: String,
    ) : TariffsScreenDialogState

    data class CreateTariff(
        val name: String,
        val interestRate: String,
    ) : TariffsScreenDialogState {
        val isDataValid: Boolean
            get() = name.isNotBlank() && interestRate.isNotBlank() && interestRate.toFloatOrNull() != null

        companion object {
            val EMPTY = CreateTariff(name = "", interestRate = "")
        }
    }
}

fun TariffsScreenDialogState.updateIfCreateTariff(
    updater: (TariffsScreenDialogState.CreateTariff) -> TariffsScreenDialogState.CreateTariff
): TariffsScreenDialogState {
    return if (this is TariffsScreenDialogState.CreateTariff) {
        updater(this)
    } else {
        this
    }
}

fun TariffsScreenDialogState.getCreateTariff(): TariffsScreenDialogState.CreateTariff? {
    return if (this is TariffsScreenDialogState.CreateTariff) {
        this
    } else {
        null
    }
}

fun TariffsScreenDialogState.getDeleteTariff(): TariffsScreenDialogState.DeleteTariff? {
    return if (this is TariffsScreenDialogState.DeleteTariff) {
        this
    } else {
        null
    }
}