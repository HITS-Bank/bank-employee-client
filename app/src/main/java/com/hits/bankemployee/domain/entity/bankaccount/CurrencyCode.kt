package com.hits.bankemployee.domain.entity.bankaccount

import ru.hitsbank.bank_common.domain.entity.CurrencyCode as CommonCurrencyCode

enum class CurrencyCode {
    RUB,
    KZT,
    CNY,
    ;
}

fun CurrencyCode.toCommonModel(): CommonCurrencyCode {
    return when (this) {
        CurrencyCode.RUB -> CommonCurrencyCode.RUB
        CurrencyCode.KZT -> CommonCurrencyCode.KZT
        CurrencyCode.CNY -> CommonCurrencyCode.CNY
    }
}