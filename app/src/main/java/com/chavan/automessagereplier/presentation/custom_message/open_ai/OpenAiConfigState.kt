package com.chavan.automessagereplier.presentation.custom_message.open_ai

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import com.chavan.automessagereplier.data.local.open_ai.OpenAiModelEnum

data class OpenAiConfigState(
    val openAiApiKey: MutableState<String> = mutableStateOf(""),
    val openAiModel: MutableState<OpenAiModelEnum> = mutableStateOf(OpenAiModelEnum.GPT_3_5_TURBO),
    val temperature : MutableState<Double> = mutableDoubleStateOf(0.7),
    val errorMessage : MutableState<String> = mutableStateOf(""),
    val isConfigSuccessFully : MutableState<Boolean> = mutableStateOf(false)
)