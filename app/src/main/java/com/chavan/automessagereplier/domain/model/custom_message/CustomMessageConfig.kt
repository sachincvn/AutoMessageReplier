package com.chavan.automessagereplier.domain.model.custom_message

data class CustomMessageConfig(
    val isActive: Boolean = true,
    val chatGptApiKey: String? = null,
)