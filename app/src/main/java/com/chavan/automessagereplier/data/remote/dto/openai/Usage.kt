package com.chavan.automessagereplier.data.remote.dto.openai

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)