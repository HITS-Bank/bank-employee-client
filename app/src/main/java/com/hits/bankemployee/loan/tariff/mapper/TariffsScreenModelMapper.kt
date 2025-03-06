package com.hits.bankemployee.loan.tariff.mapper

import com.hits.bankemployee.core.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffEntity
import com.hits.bankemployee.loan.tariff.model.TariffModel
import com.hits.bankemployee.loan.tariff.model.TariffsScreenDialogState

class TariffsScreenModelMapper {

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