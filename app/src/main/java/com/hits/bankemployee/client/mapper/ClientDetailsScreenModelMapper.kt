package com.hits.bankemployee.client.mapper

import androidx.compose.material3.MaterialTheme
import com.hits.bankemployee.client.model.BankAccountStatus
import com.hits.bankemployee.client.model.ClientDetailsListItem
import com.hits.bankemployee.client.model.toStatus
import com.hits.bankemployee.core.domain.entity.bankaccount.BankAccountEntity
import com.hits.bankemployee.core.domain.entity.loan.LoanEntity

class ClientDetailsScreenModelMapper {

    fun map(bankAccountEntity: BankAccountEntity): ClientDetailsListItem.BankAccountModel {
        return ClientDetailsListItem.BankAccountModel(
            number = bankAccountEntity.number,
            balance = bankAccountEntity.balance,
            description = when (bankAccountEntity.status.toStatus()) {
                BankAccountStatus.OPEN -> "Баланс: ${bankAccountEntity.balance} ₽"
                BankAccountStatus.CLOSED -> "Закрыт"
                BankAccountStatus.BLOCKED -> "Заблокирован"
            },
            status = bankAccountEntity.status.toStatus(),
            descriptionColorProvider = {
                when (bankAccountEntity.status.toStatus()) {
                    BankAccountStatus.OPEN -> MaterialTheme.colorScheme.onSurfaceVariant
                    BankAccountStatus.CLOSED -> MaterialTheme.colorScheme.onPrimaryContainer
                    BankAccountStatus.BLOCKED -> MaterialTheme.colorScheme.onErrorContainer
                }
            }
        )
    }

    fun map(loanEntity: LoanEntity): ClientDetailsListItem.LoanModel {
        return ClientDetailsListItem.LoanModel(
            number = loanEntity.number,
            description = "Долг: ${loanEntity.currentDebt} ₽",
            descriptionColorProvider = {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}