package com.hits.bankemployee.presentation.common

fun String?.formatToSum(): String {
    if (this == null) return ""

    val integralPart = this.substringBefore(".")
    var fractionalPart = this.substringAfter(".", "")
    if (fractionalPart.length == fractionalPart.count { it == '0' }) {
        fractionalPart = ""
    }

    val digitsOnly = integralPart.replace("\\D".toRegex(), "")
    if (digitsOnly.isEmpty()) return "0 ₽"

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
        formattedAmount.reverse().toString() + "," + fractionalPart + " ₽"
    } else {
        formattedAmount.reverse().toString() + " ₽"
    }
}