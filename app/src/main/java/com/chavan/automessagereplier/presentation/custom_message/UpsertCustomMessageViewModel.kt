package com.chavan.automessagereplier.presentation.custom_message

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.domain.usecase.DeleteCustomMessageUseCase
import com.chavan.automessagereplier.domain.usecase.UpsertCustomMessageUseCase
import com.chavan.automessagereplier.presentation.home.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UpsertCustomMessageViewModel @Inject constructor(
    private val upsCustomMessageUseCase: UpsertCustomMessageUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<UpsertCustomMessageState> = MutableStateFlow(UpsertCustomMessageState())
    val state: StateFlow<UpsertCustomMessageState> = _state.asStateFlow()

    fun onEvent(event: UpsertCustomMessageEvents) {
      when(event){
          is UpsertCustomMessageEvents.UpsertCustomMessage -> {
              viewModelScope.launch {
                  _state.value = _state.value.copy(isLoading = true)
                  upsCustomMessageUseCase(event.customMessage).collectLatest {
                      when(it){
                          is Resource.Error -> {
                              _state.value = _state.value.copy(isLoading = false, error = it.message)
                          }
                          is Resource.Loading -> {
                              _state.value = _state.value.copy(isLoading = true)
                          }
                          is Resource.Success -> {
                              _state.value = _state.value.copy(isLoading = false, isCustomMessageAdded = true)
                          }
                      }
                  }
              }
          }
      }
    }
}