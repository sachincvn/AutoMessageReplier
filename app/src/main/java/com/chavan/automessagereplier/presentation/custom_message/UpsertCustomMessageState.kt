package com.chavan.automessagereplier.presentation.custom_message

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig

data class UpsertCustomMessageState(
    val id : MutableState<Long> = mutableLongStateOf(0),
    val isEditing: MutableState<Boolean> = mutableStateOf(false),
    val receivedMessage: MutableState<String> = mutableStateOf("*"),
    val replyMessage: MutableState<String> = mutableStateOf(""),
    val isActive: MutableState<Boolean> = mutableStateOf(true),
    val receivedPattern: MutableState<ReceivedPattern> = mutableStateOf(ReceivedPattern.AnyMessage),
    val replyToOption: MutableState<ReplyToOption> = mutableStateOf(ReplyToOption.All),
    val selectedContacts: MutableState<List<String>> = mutableStateOf(emptyList()),
    val selectedContactName: MutableState<String> = mutableStateOf(""),
    val replyWithChatGPT: MutableState<Boolean> = mutableStateOf(false),
    val isApiKeyAdded: MutableState<Boolean> = mutableStateOf(false),
    val openAiConfig: MutableState<OpenAiConfig?> = mutableStateOf(null),
    val isLoading: Boolean = false,
    val isCustomMessageAdded: Boolean = false,
    val customMessage: CustomMessage? = null,
    val error: String? = null,
)