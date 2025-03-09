package com.hits.bankemployee.domain.entity.bankaccount

data class BankAccountEntity(
    val number: String,
    val balance: String,
    val status: BankAccountStatusEntity,
)