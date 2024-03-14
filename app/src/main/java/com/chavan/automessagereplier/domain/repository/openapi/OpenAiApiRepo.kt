package com.chavan.automessagereplier.domain.repository.openapi

import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO

interface OpenAiApiRepo {
    suspend fun getOpenAiResponse(chatRequestDTO: ChatRequestDTO) : OpenAiGptDTO
}