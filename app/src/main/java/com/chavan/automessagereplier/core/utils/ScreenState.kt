package com.chavan.automessagereplier.core.utils
sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    data class Error(val message: String?) : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
}