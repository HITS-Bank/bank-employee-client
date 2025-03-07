package com.hits.bankemployee.client.mapper

import com.hits.bankemployee.client.model.ClientDetailsListItem
import com.hits.bankemployee.client.model.toStatus
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanEntity

class ClientDetailsScreenMapper {

    fun map(bankAccountEntity: BankAccountEntity): ClientDetailsListItem.BankAccountModel {
        return ClientDetailsListItem.BankAccountModel(
            number = bankAccountEntity.number,
            balance = bankAccountEntity.balance,
            status = bankAccountEntity.status.toStatus(),
        )
    }

    fun map(loanEntity: LoanEntity): ClientDetailsListItem.LoanModel {
        return ClientDetailsListItem.LoanModel(
            number = loanEntity.number,
            currentDebt = loanEntity.currentDebt,
        )
    }
}