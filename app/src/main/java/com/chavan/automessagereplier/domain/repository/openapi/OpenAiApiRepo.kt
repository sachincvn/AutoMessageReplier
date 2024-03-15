package com.chavan.automessagereplier.domain.repository.openapi

import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig

interface OpenAiApiRepo {
    suspend fun getOpenAiResponse(apiKey : String,chatRequestDTO: ChatRequestDTO) : OpenAiGptDTO
    suspend fun getOpenAiLocalConfig() : OpenAiConfig?
    suspend fun upsertOpenAiConfig(openAiConfig: OpenAiConfig)
}