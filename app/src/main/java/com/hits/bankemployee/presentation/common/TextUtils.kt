package com.hits.bankemployee.presentation.common

import com.hits.bankemployee.domain.entity.bankaccount.CurrencyCode

private fun  CurrencyCode.toSymbol(): Char {
    return when (this) {
        CurrencyCode.RUB -> '₽'
        CurrencyCode.KZT -> '₸'
        CurrencyCode.CNY -> '¥'
    }
}

fun String?.formatToSum(currencyCode: CurrencyCode): String {
    if (this == null) return ""

    val integralPart = this.substringBefore(".")
    var fractionalPart = this.substringAfter(".", "")
    if (fractionalPart.length == fractionalPart.count { it == '0' }) {
        fractionalPart = ""
    }

    val digitsOnly = integralPart.replace("\\D".toRegex(), "")
    if (digitsOnly.isEmpty()) return "0 ${currencyCode.toSymbol()}"

    val formattedAmount = StringBuilder()
    var count = 0
    for (i in digitsOnly.length - 1 downTo 0) {
        formattedAmount.append(digitsOnly[i])
        count++
        if (count % 3 == 0 && i != 0) {
            formattedAmount.append(' ')
        }
    }

    return if (fractionalPart.isNotBlank()) {
        formattedAmount.reverse().toString() + "," + fractionalPart + " ${currencyCode.toSymbol()}"
    } else {
        formattedAmount.reverse().toString() + " ${currencyCode.toSymbol()}"
    }
}