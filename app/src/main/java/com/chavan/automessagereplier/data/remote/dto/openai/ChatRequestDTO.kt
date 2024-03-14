package com.chavan.automessagereplier.data.remote.dto.openai

data class ChatRequestDTO(
    val messages: List<MessageX>,
    val model: String,
    val temperature: Double
)