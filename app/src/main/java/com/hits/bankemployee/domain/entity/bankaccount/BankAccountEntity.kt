package com.hits.bankemployee.domain.entity.bankaccount

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class BankAccountEntity(
    val id: String,
    val number: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val status: BankAccountStatusEntity,
)