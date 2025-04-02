package com.hits.bankemployee.presentation.screen.loan.payments.mapper

import com.hits.bankemployee.domain.entity.loan.LoanPaymentEntity
import com.hits.bankemployee.domain.entity.loan.LoanPaymentStatus
import com.hits.bankemployee.presentation.screen.loan.payments.model.LoanPaymentItem
import ru.hitsbank.bank_common.presentation.common.formatToSum
import ru.hitsbank.bank_common.presentation.common.toSymbol
import ru.hitsbank.bank_common.presentation.common.utcDateTimeToReadableFormat
import ru.hitsbank.bank_common.presentation.theme.grayBackground
import ru.hitsbank.bank_common.presentation.theme.grayForeground
import ru.hitsbank.bank_common.presentation.theme.topUpBackground
import ru.hitsbank.bank_common.presentation.theme.topUpForeground
import ru.hitsbank.bank_common.presentation.theme.withdrawBackground
import ru.hitsbank.bank_common.presentation.theme.withdrawForeground
import javax.inject.Inject

class LoanPaymentsMapper @Inject constructor() {

    fun map(loanPaymentEntity: LoanPaymentEntity): LoanPaymentItem {
        return LoanPaymentItem(
            id = loanPaymentEntity.id,
            title = loanPaymentEntity.dateTime.utcDateTimeToReadableFormat(),
            description = loanPaymentEntity.amount.formatToSum(loanPaymentEntity.currencyCode, true),
            status = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> "Запланирован"
                LoanPaymentStatus.OVERDUE -> "Просрочен"
                LoanPaymentStatus.EXECUTED -> "Оплачен"
            },
            currencyChar = loanPaymentEntity.currencyCode.toSymbol(),
            foregroundColor = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> grayForeground
                LoanPaymentStatus.OVERDUE -> withdrawForeground
                LoanPaymentStatus.EXECUTED -> topUpForeground
            },
            backgroundColor = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> grayBackground
                LoanPaymentStatus.OVERDUE -> withdrawBackground
                LoanPaymentStatus.EXECUTED -> topUpBackground
            },
        )
    }
}