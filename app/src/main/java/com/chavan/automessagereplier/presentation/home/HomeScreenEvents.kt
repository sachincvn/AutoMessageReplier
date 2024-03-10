package com.chavan.automessagereplier.presentation.home

import com.chavan.automessagereplier.domain.model.CustomMessage

sealed class HomeScreenEvents {
    data class DeleteCustomMessage(val id: Long) : HomeScreenEvents()
    object Refresh : HomeScreenEvents()
    data class  ToggleActive(val customMessage: CustomMessage) : HomeScreenEvents()
}