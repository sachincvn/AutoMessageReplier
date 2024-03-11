package com.chavan.automessagereplier.presentation.custom_message

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.domain.model.CustomMessage

data class UpsertCustomMessageState(
    val receivedMessage : MutableState<String> = mutableStateOf(""),
    val replyMessage : MutableState<String> = mutableStateOf(""),
    val isActive : MutableState<Boolean> = mutableStateOf(true),
    val receivedPattern : MutableState<ReceivedPattern> = mutableStateOf(ReceivedPattern.ExactMatch),
    val replyToOption: MutableState<ReplyToOption> = mutableStateOf(ReplyToOption.All),
    val selectedContacts : MutableState<List<String>> = mutableStateOf(emptyList()),
    val replyWithChatGPT : MutableState<Boolean> = mutableStateOf(false),
    val isLoading: Boolean = false,
    val isCustomMessageAdded : Boolean = false,
    val customMessage: CustomMessage? = null,
    val error: String? = null,
)