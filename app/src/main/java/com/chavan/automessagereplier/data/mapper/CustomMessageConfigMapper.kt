package com.chavan.automessagereplier.data.mapper

import com.chavan.automessagereplier.data.local.custom_message.CustomMessageConfigEntity
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessageConfig

fun CustomMessageConfigEntity.toCustomMessage(): CustomMessageConfig {
    return CustomMessageConfig(
        isActive = isActive,
        chatGptApiKey = chatGptApiKey
    )
}

fun CustomMessageConfig.toCustomMessageEntity(): CustomMessageConfigEntity {
    return CustomMessageConfigEntity(
        isActive = isActive,
        chatGptApiKey = chatGptApiKey
    )
}