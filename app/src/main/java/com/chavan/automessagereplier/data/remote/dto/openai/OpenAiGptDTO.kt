package com.chavan.automessagereplier.data.remote.dto.openai

import com.chavan.autochat.data.remote.dto.openai.Choice
import com.chavan.automessagereplier.domain.model.openai.OpenAiResponseModel

data class OpenAiGptDTO(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

fun OpenAiGptDTO.toOpenApiResponse() : OpenAiResponseModel {
    return OpenAiResponseModel(
        choices = choices,
        created = created,
        id  = id,
        model = model,
        `object` = `object`
    )
}