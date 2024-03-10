package com.chavan.automessagereplier.presentation

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}
