package com.chavan.automessagereplier.presentation.home

import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage

sealed class HomeScreenEvents {
    data class DeleteCustomMessage(val id: Long) : HomeScreenEvents()
    object Refresh : HomeScreenEvents()
    data class ToggleActive(val customMessage: CustomMessage) : HomeScreenEvents()
    data class ToggleAutoReplier(val isActive: Boolean) : HomeScreenEvents()
}