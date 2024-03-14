package com.chavan.automessagereplier.domain.model.openai

import com.chavan.autochat.data.remote.dto.openai.Choice

data class OpenAiResponseModel(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String
)