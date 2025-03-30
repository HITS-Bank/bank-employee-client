package com.hits.bankemployee.presentation.screen.loan.tariff.mapper

import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.presentation.screen.loan.tariff.model.TariffModel
import com.hits.bankemployee.presentation.screen.loan.tariff.model.TariffsScreenDialogState
import javax.inject.Inject

class TariffsScreenModelMapper @Inject constructor() {

    fun map(loanTariffEntity: LoanTariffEntity): TariffModel {
        return TariffModel(
            id = loanTariffEntity.id,
            name = loanTariffEntity.name,
            interestRate = "${loanTariffEntity.interestRate}%",
        )
    }

    fun map(dialogState: TariffsScreenDialogState.CreateTariff): LoanTariffCreateRequestEntity {
        return LoanTariffCreateRequestEntity(
            name = dialogState.name,
            interestRate = dialogState.interestRate,
        )
    }
}