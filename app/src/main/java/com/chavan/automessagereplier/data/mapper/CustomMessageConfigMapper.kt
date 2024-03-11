package com.chavan.automessagereplier.data.mapper

import com.chavan.automessagereplier.data.local.CustomMessageConfigEntity
import com.chavan.automessagereplier.domain.model.CustomMessageConfig

fun CustomMessageConfigEntity.toCustomMessage() : CustomMessageConfig{
    return CustomMessageConfig(
        isActive = isActive,
        chatGptApiKey = chatGptApiKey
    )
}

fun CustomMessageConfig.toCustomMessageEntity() : CustomMessageConfigEntity{
    return CustomMessageConfigEntity(
        isActive = isActive,
        chatGptApiKey = chatGptApiKey
    )
}