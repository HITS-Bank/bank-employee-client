package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.loan.LoanResponse
import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffResponse
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

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

    fun map(loanResponse: LoanResponse): LoanEntity {
        return LoanEntity(
            number = loanResponse.number,
            tariff = map(loanResponse.tariff),
            amount = loanResponse.amount,
            termInMonths = loanResponse.termInMonths,
            bankAccountNumber = loanResponse.bankAccountNumber,
            paymentAmount = loanResponse.paymentAmount,
            paymentSum = loanResponse.paymentSum,
            nextPaymentDateTime = LocalDateTime.parse(loanResponse.nexPaymentDateTime).atZone(
                ZoneOffset.UTC).toLocalDateTime(),
            currentDebt = loanResponse.currentDebt,
        )
    }
}