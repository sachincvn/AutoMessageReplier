package com.chavan.automessagereplier.presentation.custom_message

import com.chavan.automessagereplier.domain.model.CustomMessage

sealed class UpsertCustomMessageEvents {
    data class UpsertCustomMessage(val customMessage: CustomMessage) : UpsertCustomMessageEvents()
}