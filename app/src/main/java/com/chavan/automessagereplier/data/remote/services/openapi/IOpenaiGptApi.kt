package com.chavan.automessagereplier.data.remote.services.openapi

import com.chavan.automessagereplier.core.commons.Constants
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface IOpenaiGptApi {
    @Headers("Content-Type: application/json")
    @POST("/v1/chat/completions")
    suspend fun getOpenAiResponse(
        @Header("Authorization") apiKey: String,
        @Body chatRequestBody : ChatRequestDTO
    ) : OpenAiGptDTO
}