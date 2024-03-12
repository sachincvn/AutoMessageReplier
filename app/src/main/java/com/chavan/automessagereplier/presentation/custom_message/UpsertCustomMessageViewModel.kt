package com.chavan.automessagereplier.presentation.custom_message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.usecase.UpsertCustomMessageUseCase
import com.chavan.automessagereplier.presentation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertCustomMessageViewModel @Inject constructor(
    private val upsCustomMessageUseCase: UpsertCustomMessageUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<UpsertCustomMessageState> =
        MutableStateFlow(UpsertCustomMessageState())
    val state: StateFlow<UpsertCustomMessageState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    val receivedPattern = ReceivedPattern.values().toList()
    val replyToOption = ReplyToOption.values().toList()

    fun onEvent(event: UpsertCustomMessageEvents) {
        when (event) {
            is UpsertCustomMessageEvents.UpsertCustomMessage -> {
                viewModelScope.launch {
                    if (!isValidForm(event.customMessage)) {
                        return@launch
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
            }
        }
    }

    private suspend fun isValidForm(customMessage: CustomMessage): Boolean {
        if (customMessage.receivedMessage.isBlank()) {
            _uiEvent.emit(UiEvent.ShowSnackbar("Received message is empty"))
            return false
        }
        if (customMessage.replyMessage.isBlank()) {
            _uiEvent.emit(UiEvent.ShowSnackbar("Received message is empty"))
            return false
        }
        return true
    }
}