package com.chavan.automessagereplier.core.utils

object StringChecker {
    fun String.isName(): Boolean {
        return this.matches(Regex("[a-zA-Z]+"))
    }

    fun String.isNumber(): Boolean {
        return this.matches(Regex("\\d+"))
    }
}
