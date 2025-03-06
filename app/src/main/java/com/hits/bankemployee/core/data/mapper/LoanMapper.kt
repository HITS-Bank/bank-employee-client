package com.hits.bankemployee.core.data.mapper

import com.hits.bankemployee.core.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.core.data.model.loan.LoanTariffResponse
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanTariffEntity

class LoanMapper {

    fun map(loanTariffResponse: LoanTariffResponse): LoanTariffEntity {
        return LoanTariffEntity(
            id = loanTariffResponse.id,
            name = loanTariffResponse.name,
            interestRate = loanTariffResponse.interestRate,
        )
    }

    fun map(loanTariffCreateRequestEntity: LoanTariffCreateRequestEntity): LoanTariffCreateRequest {
        return LoanTariffCreateRequest(
            name = loanTariffCreateRequestEntity.name,
            interestRate = loanTariffCreateRequestEntity.interestRate,
        )
    }
}