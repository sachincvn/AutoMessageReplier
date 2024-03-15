package com.chavan.automessagereplier.presentation.custom_message

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import com.chavan.automessagereplier.domain.usecase.UpsertCustomMessageUseCase
import com.chavan.automessagereplier.presentation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertCustomMessageViewModel @Inject constructor(
    private val upsCustomMessageUseCase: UpsertCustomMessageUseCase,
    private val openAiApiRepo: OpenAiApiRepo
) : ViewModel() {

    private val _state = mutableStateOf(UpsertCustomMessageState())
    val state: State<UpsertCustomMessageState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    val receivedPattern = ReceivedPattern.values().toList()
    val replyToOption = ReplyToOption.values().toList()

    private var textEnteredCount : Int = 0

    init {
        getOpenAiConfig()
    }
    fun onEvent(event: UpsertCustomMessageEvents) {
        when (event) {
            is UpsertCustomMessageEvents.UpsertCustomMessage -> {
                viewModelScope.launch {
                   upsertCustomMessage(event)
                }
            }

            is UpsertCustomMessageEvents.OnReceivedMessageChange -> {
                updateReceivedPatternOption(event);
            }

            is UpsertCustomMessageEvents.OnReceivedPatterChange -> {
                _state.value.receivedPattern.value =  event.option
                if(_state.value.receivedPattern.value == ReceivedPattern.AnyMessage) {
                    _state.value.receivedMessage.value = "*"
                }
                else{
                    _state.value.receivedMessage.value = ""
                }
            }
        }
    }

    private fun updateReceivedPatternOption(event: UpsertCustomMessageEvents.OnReceivedMessageChange) {
        textEnteredCount++
        if (event.message == "*" || _state.value.receivedMessage.value == "*"){
            _state.value = _state.value.copy(receivedPattern = mutableStateOf(ReceivedPattern.AnyMessage))
            textEnteredCount = 0
        }
        else if (textEnteredCount==1){
            _state.value = _state.value.copy(receivedPattern = mutableStateOf(ReceivedPattern.ExactMatch))
        }
    }

    private suspend fun upsertCustomMessage(event: UpsertCustomMessageEvents.UpsertCustomMessage) {
        if (!isValidForm(event.customMessage)) {
            return
        }
        _state.value = _state.value.copy(isLoading = true)
        upsCustomMessageUseCase(event.customMessage).collectLatest {
            when (it) {
                is Resource.Error -> {
                    _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            it.message ?: "Error deleting message"
                        )
                    )
                    _state.value =
                        _state.value.copy(isLoading = false, error = it.message)
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isCustomMessageAdded = true
                    )
                }
            }
        }
    }

    private suspend fun isValidForm(customMessage: CustomMessage): Boolean {
        if (customMessage.receivedMessage.isBlank()) {
            _uiEvent.emit(UiEvent.ShowSnackbar("Received message is empty"))
            return false
        }
        if (customMessage.replyMessage.isBlank()) {
            if (state.value.replyWithChatGPT.value){
                return true
            }
            _uiEvent.emit(UiEvent.ShowSnackbar("Received message is empty"))
            return false
        }
        return true
    }

    private fun getOpenAiConfig(){
        viewModelScope.launch {
            try {
                val response = openAiApiRepo.getOpenAiLocalConfig()
                if (response!=null){
                    state.value.isApiKeyAdded.value = response.openAiApiKey!!.isNotBlank()
                    state.value.openAiConfig.value = response
                }
            }
            catch (ex : Exception){
                ex.printStackTrace()
            }
        }
    }
}