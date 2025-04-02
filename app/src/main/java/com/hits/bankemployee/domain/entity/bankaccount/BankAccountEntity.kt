package com.hits.bankemployee.domain.entity.bankaccount

data class BankAccountEntity(
    val id: String,
    val number: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val status: BankAccountStatusEntity,
)