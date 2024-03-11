package com.chavan.automessagereplier.core.utils

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumWithStringValue(val value: String)