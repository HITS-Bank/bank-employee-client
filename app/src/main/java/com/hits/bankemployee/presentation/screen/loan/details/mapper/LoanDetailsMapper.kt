package com.hits.bankemployee.presentation.screen.loan.details.mapper

import com.hits.bankemployee.domain.entity.loan.LoanEntity
import com.hits.bankemployee.presentation.common.formatToSum
import com.hits.bankemployee.presentation.screen.loan.details.model.LoanDetailsListItem
import java.time.format.DateTimeFormatter

class LoanDetailsMapper {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun map(loan: LoanEntity): List<LoanDetailsListItem> {
        return listOf(
            LoanDetailsListItem.LoanInfoHeader,
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.number,
                name = "Номер кредита",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.tariff.name,
                name = "Название тарифа",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.tariff.interestRate,
                name = "Процентная ставка",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.termInMonths.toString(),
                name = "Срок кредита (мес.)",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.amount.formatToSum(),
                name = "Сумма кредита",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.paymentSum.formatToSum(),
                name = "Сумма выплат"
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.paymentAmount.formatToSum(),
                name = "Сумма платежа"
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = dateTimeFormatter.format(loan.nextPaymentDateTime),
                name = "Время следующего платежа",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.currentDebt.formatToSum(),
                name = "Текущий долг",
            ),
            LoanDetailsListItem.LoanBankAccount(
                value = loan.bankAccountNumber,
                name = "Счет кредита",
                accountNumber = loan.bankAccountNumber,
            ),
        )
    }
}