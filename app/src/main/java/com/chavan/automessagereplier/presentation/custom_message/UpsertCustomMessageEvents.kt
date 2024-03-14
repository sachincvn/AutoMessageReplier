package com.chavan.automessagereplier.presentation.custom_message

import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.domain.model.CustomMessage

sealed class UpsertCustomMessageEvents {
    data class UpsertCustomMessage(val customMessage: CustomMessage) : UpsertCustomMessageEvents()
    data class OnReceivedMessageChange(val message : String) : UpsertCustomMessageEvents()
    data class OnReceivedPatterChange(val option : ReceivedPattern) : UpsertCustomMessageEvents()

}