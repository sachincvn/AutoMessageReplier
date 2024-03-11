package com.chavan.automessagereplier.data.mapper

import com.chavan.automessagereplier.data.local.CustomMessageEntity
import com.chavan.automessagereplier.domain.model.CustomMessage

fun CustomMessageEntity.toCustomMessage() : CustomMessage{
    return CustomMessage(
        receivedMessage = receivedMessage,
        replyMessage = replyMessage,
        id = id,
        isActive = isActive,
        receivedPattern = receivedPattern,
        replyToOption = replyToOption,
        selectedContacts = selectedContacts
    )
}

fun CustomMessage.toCustomMessageEntity() : CustomMessageEntity{
    return CustomMessageEntity(
        receivedMessage = receivedMessage,
        replyMessage = replyMessage,
        id = id,
        isActive = isActive,
        receivedPattern = receivedPattern,
        replyToOption = replyToOption,
        selectedContacts = selectedContacts
    )
}