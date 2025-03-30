package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.loan.LoanResponse
import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffResponse
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class LoanMapper @Inject constructor() {

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
            id = loanResponse.id,
            number = loanResponse.number,
            tariff = map(loanResponse.tariff),
            amount = loanResponse.amount,
            termInMonths = loanResponse.termInMonths,
            bankAccountId = loanResponse.bankAccountId,
            bankAccountNumber = loanResponse.bankAccountNumber,
            paymentAmount = loanResponse.paymentAmount,
            paymentSum = loanResponse.paymentSum,
            currencyCode = loanResponse.currencyCode,
            nextPaymentDateTime = LocalDateTime.parse(loanResponse.nextPaymentDateTime).atZone(
                ZoneOffset.UTC).toLocalDateTime(),
            currentDebt = loanResponse.currentDebt,
        )
    }
}