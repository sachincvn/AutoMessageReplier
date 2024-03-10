package com.chavan.automessagereplier.domain.model

data class CustomMessage(
    val receivedMessage : String,
    val replyMessage : String,
    val isActive : Boolean = false,
    val id : Long = 0
)
