package com.chavan.automessagereplier.data.remote.services.openapi

import com.chavan.automessagereplier.core.commons.Constants
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IOpenaiGptApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${Constants.openApiKey}"
    )
    @POST("/v1/chat/completions")
    suspend fun getOpenAiResponse(
        @Body chatRequestBody : ChatRequestDTO
    ) : OpenAiGptDTO
}