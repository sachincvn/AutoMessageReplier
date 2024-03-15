package com.chavan.automessagereplier.data.mapper

import com.chavan.automessagereplier.data.local.open_ai.OpenAiConfigEntity
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig

fun OpenAiConfig.toOpenAiConfigDaoEntity() : OpenAiConfigEntity{
    return OpenAiConfigEntity(
        openAiApiKey = openAiApiKey,
        openAiModel = openAiModel,
        temperature = temperature,
        errorMessage = errorMessage,
        id = id
    )
}

fun OpenAiConfigEntity.toOpenAiConfigDaoEntity() : OpenAiConfig{
    return OpenAiConfig(
        openAiApiKey = openAiApiKey,
        openAiModel = openAiModel,
        temperature = temperature,
        errorMessage = errorMessage,
        id = id
    )
}