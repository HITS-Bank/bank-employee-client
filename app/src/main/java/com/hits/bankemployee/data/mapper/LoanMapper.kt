package com.hits.bankemployee.data.mapper

import com.hits.bankemployee.data.model.loan.LoanPaymentResponse
import com.hits.bankemployee.data.model.loan.LoanResponse
import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffResponse
import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.domain.entity.loan.LoanPaymentEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffCreateRequestEntity
import com.hits.bankemployee.domain.entity.loan.LoanTariffEntity
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
            nextPaymentDateTime = loanResponse.nextPaymentDateTime,
            currentDebt = loanResponse.currentDebt,
        )
    }

    fun map(loanPaymentResponse: LoanPaymentResponse): LoanPaymentEntity {
        return LoanPaymentEntity(
            id = loanPaymentResponse.id,
            status = loanPaymentResponse.status,
            dateTime = loanPaymentResponse.dateTime,
            amount = loanPaymentResponse.amount,
            currencyCode = loanPaymentResponse.currencyCode,
        )
    }
}