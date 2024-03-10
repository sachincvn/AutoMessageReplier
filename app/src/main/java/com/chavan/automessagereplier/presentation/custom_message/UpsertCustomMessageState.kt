package com.chavan.automessagereplier.presentation.custom_message

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.chavan.automessagereplier.domain.model.CustomMessage

data class UpsertCustomMessageState(
    val receivedMessage : MutableState<String> = mutableStateOf(""),
    val replyMessage : MutableState<String> = mutableStateOf(""),
    val isLoading: Boolean = false,
    val isCustomMessageAdded : Boolean = false,
    val customMessage: CustomMessage? = null,
    val error: String? = null,
)