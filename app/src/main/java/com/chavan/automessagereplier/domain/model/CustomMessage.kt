package com.chavan.automessagereplier.domain.model

import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption

data class CustomMessage(
    val receivedMessage: String,
    val replyMessage: String,
    val receivedPattern: ReceivedPattern = ReceivedPattern.ExactMatch,
    val replyToOption: ReplyToOption = ReplyToOption.SavedContact,
    val isActive: Boolean = false,
    val selectedContacts: List<String> = emptyList(),
    val replyWithChatGptApi : Boolean = false,
    val id: Long = 0
)
