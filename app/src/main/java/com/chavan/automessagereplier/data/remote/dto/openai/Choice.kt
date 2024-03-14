package com.chavan.autochat.data.remote.dto.openai

import com.chavan.automessagereplier.data.remote.dto.openai.Message

data class Choice(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val message: Message
)