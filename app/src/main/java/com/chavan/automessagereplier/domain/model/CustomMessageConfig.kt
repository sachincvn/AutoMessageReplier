package com.chavan.automessagereplier.domain.model

data class CustomMessageConfig(
    val isActive: Boolean = true,
    val chatGptApiKey: String? = null,
)