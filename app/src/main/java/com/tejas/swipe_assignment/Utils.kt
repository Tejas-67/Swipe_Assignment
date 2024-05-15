package com.tejas.swipe_assignment

fun Float.toCurrencyNotation(): String = "₹$this"

fun Float.toTaxNotation(): String = "$this%"

fun String.trimAndCapitalizeFirstChar(): String {
    val trimmedString = this.trim()

    if (trimmedString.isEmpty()) return trimmedString

    val firstChar = trimmedString[0]

    return if (firstChar.isLetter()) {
        firstChar.uppercaseChar() + trimmedString.substring(1)
    } else {
        trimmedString
    }
}