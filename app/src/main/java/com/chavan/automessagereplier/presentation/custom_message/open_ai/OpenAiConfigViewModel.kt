package com.chavan.automessagereplier.presentation.custom_message.open_ai

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import com.chavan.automessagereplier.presentation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenAiConfigViewModel @Inject constructor(
    private val openAiApiRepo: OpenAiApiRepo
) : ViewModel() {

    private val _state = mutableStateOf(OpenAiConfigState())
    val state: State<OpenAiConfigState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    fun onEvent(openAiConfigEvents: OpenAiConfigEvents) {
        when (openAiConfigEvents) {
            OpenAiConfigEvents.UpsertOpenAiConfig -> {
                upsertOpenAiConfig()
            }
        }
    }

    private fun upsertOpenAiConfig() {
        viewModelScope.launch {
            if (isValidForm()) {
                try {
                    val openAiConfig = OpenAiConfig(
                        openAiApiKey = _state.value.openAiApiKey.value,
                        temperature = _state.value.temperature.value.toDouble(),
                        openAiModel = _state.value.openAiModel.value,
                        errorMessage = _state.value.errorMessage.value
                    )
                    openAiApiRepo.upsertOpenAiConfig(openAiConfig)
                    _state.value.isConfigSuccessFully.value = true
                } catch (ex: Exception) {
                    _state.value.isConfigSuccessFully.value = false
                    ex.printStackTrace()
                }
            }
        }
    }

    private suspend fun isValidForm(): Boolean {
        state.value.isOpenAiApiKeyEmpty.value = state.value.openAiApiKey.value.isBlank()
        state.value.isOpenTemperatureEmpty.value = state.value.temperature.value.isBlank()
        if (state.value.openAiApiKey.value.isBlank()) {
            _uiEvent.emit(UiEvent.ShowSnackbar("Api Key cannot be empty"))
            return false
        }
        if (state.value.temperature.value.isBlank()) {
            _uiEvent.emit(UiEvent.ShowSnackbar("Temperature cannot be empty"))
            return false
        }
        return true
    }
}