package com.chavan.automessagereplier.presentation.home

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessageConfig
import com.chavan.automessagereplier.domain.usecase.ConfigCustomMessageUseCase
import com.chavan.automessagereplier.domain.usecase.DeleteCustomMessageUseCase
import com.chavan.automessagereplier.domain.usecase.GetAllCustomMessagesUseCase
import com.chavan.automessagereplier.domain.usecase.UpsertCustomMessageUseCase
import com.chavan.automessagereplier.notification_service.NotificationUtils
import com.chavan.automessagereplier.presentation.UiEvent
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCustomMessagesUseCase: GetAllCustomMessagesUseCase,
    private val deleteCustomMessageUseCase: DeleteCustomMessageUseCase,
    private val upsertCustomMessageUseCase: UpsertCustomMessageUseCase,
    private val configCustomMessageUseCase: ConfigCustomMessageUseCase,
    private val appContext: Context,
) : ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent: SharedFlow<HomeEvent> = _homeEvent

    init {
        getCustomMessageConfig()
        getAllCustomMessages()
    }


    fun onEvent(homeScreenEvent: HomeScreenEvents) {
        when (homeScreenEvent) {
            is HomeScreenEvents.DeleteCustomMessage -> deleteCustomMessage(homeScreenEvent.id)
            HomeScreenEvents.Refresh -> getAllCustomMessages()
            is HomeScreenEvents.ToggleActive -> toggleActive(homeScreenEvent.customMessage)
            is HomeScreenEvents.ToggleAutoReplier -> toggleAutoReplier(homeScreenEvent.isActive)
        }
    }

    private fun toggleAutoReplier(active: Boolean) {
        viewModelScope.launch {
            if (NotificationUtils.isNotificationAccessGranted(
                    appContext, appContext.packageName
                )) {
                configCustomMessageUseCase(customMessageConfig = CustomMessageConfig(isActive = active))
                    .collectLatest {
                        when (it) {
                            is Resource.Error -> _uiEvent.emit(
                                UiEvent.ShowSnackbar(
                                    it.message ?: "Something went wrong"
                                )
                            )

                            is Resource.Success -> {
                                _state.value = _state.value.copy(isAutoMessagingActive = active)
                            }

                            else -> return@collectLatest
                        }
                    }
            } else {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please grant notification access to enable auto messaging"))
                _homeEvent.emit(HomeEvent.RequestNotificationAccess)
            }
        }
    }

    private fun toggleActive(customMessage: CustomMessage) {
        viewModelScope.launch {
            upsertCustomMessageUseCase(customMessage.copy(isActive = !customMessage.isActive)).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val updatedMessages = _state.value.customMessages.map {
                            if (it.id == customMessage.id) {
                                it.copy(isActive = !customMessage.isActive)
                            } else {
                                it
                            }
                        }
                        _state.value = _state.value.copy(customMessages = updatedMessages)
                    }

                    is Resource.Error -> {
                        UiEvent.ShowSnackbar(
                            result.message ?: "Error deleting message"
                        )
                    }

                    else -> return@collectLatest
                }
            }
        }
    }

    private fun deleteCustomMessage(id: Long) {
        viewModelScope.launch {
            deleteCustomMessageUseCase(id)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _uiEvent.emit(
                                UiEvent.ShowSnackbar(
                                    result.message ?: "Error deleting message"
                                )
                            )
                        }

                        is Resource.Success -> {
                            val updatedMessages = _state.value.customMessages.filter { it.id != id }
                            _state.value = _state.value.copy(customMessages = updatedMessages)
                        }

                        else -> return@collectLatest
                    }
                }
        }
    }

    private fun getCustomMessageConfig() {
        viewModelScope.launch {
            configCustomMessageUseCase.getCustomMessageConfig().collectLatest {
                when (it) {
                    is Resource.Error -> _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            it.message ?: "Something went wrong"
                        )
                    )

                    is Resource.Success -> {
                        _state.value = _state.value.copy(isAutoMessagingActive = it.data!!.isActive)
                    }

                    else -> return@collectLatest
                }
            }
        }
    }

    private fun getAllCustomMessages() {
        viewModelScope.launch {
            getAllCustomMessagesUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(isLoading = false, errorMessage = result.message)
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            customMessages = result.data!!
                        )
                    }
                }
            }
        }
    }

}

sealed class HomeEvent {
    object RequestNotificationAccess : HomeEvent()
}
