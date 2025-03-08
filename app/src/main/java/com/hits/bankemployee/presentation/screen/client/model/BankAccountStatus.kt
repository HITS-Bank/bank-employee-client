package com.hits.bankemployee.presentation.screen.client.model

import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity

enum class BankAccountStatus {
    OPEN,
    CLOSED,
    BLOCKED,
}

fun BankAccountStatusEntity.toStatus(): BankAccountStatus {
    return when (this) {
        BankAccountStatusEntity.OPEN -> BankAccountStatus.OPEN
        BankAccountStatusEntity.CLOSED -> BankAccountStatus.CLOSED
        BankAccountStatusEntity.BLOCKED -> BankAccountStatus.BLOCKED
    }
}

fun BankAccountStatus.toEntity(): BankAccountStatusEntity {
    return when (this) {
        BankAccountStatus.OPEN -> BankAccountStatusEntity.OPEN
        BankAccountStatus.CLOSED -> BankAccountStatusEntity.CLOSED
        BankAccountStatus.BLOCKED -> BankAccountStatusEntity.BLOCKED
    }
}