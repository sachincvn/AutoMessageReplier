package com.chavan.automessagereplier.data.repository.openai

import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import com.chavan.automessagereplier.data.remote.services.openapi.IOpenaiGptApi
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import javax.inject.Inject
class OpenAiRepoImpl @Inject constructor(
    private val api: IOpenaiGptApi
)  : OpenAiApiRepo {

    override suspend fun getOpenAiResponse(chatRequestDTO: ChatRequestDTO): OpenAiGptDTO {
        val response = api.getOpenAiResponse(chatRequestDTO)
        var message  = response.choices[0].message.content
        return  response
    }

}