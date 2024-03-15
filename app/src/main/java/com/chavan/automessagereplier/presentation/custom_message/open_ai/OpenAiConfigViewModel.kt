package com.chavan.automessagereplier.presentation.custom_message.open_ai

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.data.local.open_ai.OpenAiModelEnum
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenAiConfigViewModel @Inject constructor(
    private val openAiApiRepo: OpenAiApiRepo
) : ViewModel() {

    private val _state = mutableStateOf(OpenAiConfigState())
    val state: State<OpenAiConfigState> = _state

    init {
        getOpenAiConfig()
    }

    fun onEvent(openAiConfigEvents: OpenAiConfigEvents){
        when(openAiConfigEvents){
            OpenAiConfigEvents.UpsertOpenAiConfig -> {
                upsertOpenAiConfig()
            }
        }
    }

    private fun getOpenAiConfig(){
        viewModelScope.launch {
            try {
                val result = openAiApiRepo.getOpenAiLocalConfig()
                if (result!=null){
                    _state.value.openAiApiKey.value = result.openAiApiKey ?: ""
                    _state.value.openAiModel.value = result.openAiModel ?: OpenAiModelEnum.GPT_3_5_TURBO
                    _state.value.temperature.value = result.temperature ?: 0.7
                    _state.value.errorMessage.value = result.errorMessage ?: ""
                }
            }
            catch (ex : Exception){
                ex.printStackTrace()
            }
        }
    }

    private fun upsertOpenAiConfig(){
        viewModelScope.launch {
            val openAiConfig = OpenAiConfig(
                openAiApiKey = _state.value.openAiApiKey.value,
                temperature = _state.value.temperature.value,
                openAiModel = _state.value.openAiModel.value,
                errorMessage = _state.value.errorMessage.value
            )
            openAiApiRepo.upsertOpenAiConfig(openAiConfig)
        }
    }
}