package com.chavan.automessagereplier.domain.model.openai

import com.chavan.automessagereplier.data.local.open_ai.OpenAiModelEnum
data class OpenAiConfig(
    val openAiApiKey : String? = null,
    val openAiModel : OpenAiModelEnum = OpenAiModelEnum.GPT_3_5_TURBO,
    val temperature : Double = 0.7,
    val errorMessage : String? = null
)