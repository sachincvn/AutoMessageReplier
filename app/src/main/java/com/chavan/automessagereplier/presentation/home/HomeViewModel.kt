package com.chavan.automessagereplier.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.usecase.DeleteCustomMessageUseCase
import com.chavan.automessagereplier.domain.usecase.GetAllCustomMessagesUseCase
import com.chavan.automessagereplier.domain.usecase.UpsertCustomMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCustomMessagesUseCase: GetAllCustomMessagesUseCase,
    private val deleteCustomMessageUseCase: DeleteCustomMessageUseCase,
    private val upsertCustomMessageUseCase: UpsertCustomMessageUseCase
) : ViewModel(){

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    init {
        getAllCustomMessages()
    }

    fun onEvent(homeScreenEvent: HomeScreenEvents){
        when(homeScreenEvent){
            is HomeScreenEvents.DeleteCustomMessage ->{
                deleteCustomMessage(homeScreenEvent.id)
            }
            HomeScreenEvents.Refresh -> {
                getAllCustomMessages()
            }
            is HomeScreenEvents.ToggleActive -> {
                toggleActive(homeScreenEvent.customMessage)
            }
        }
    }

    private fun toggleActive(customMessage: CustomMessage) {
        viewModelScope.launch {
            upsertCustomMessageUseCase(customMessage.copy(isActive = !customMessage.isActive)).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                        ScreenState.Loading
                    }
                    is Resource.Success -> {
                        val updatedList = when (val screenState = state.value.result) {
                            is ScreenState.Success -> {
                                val updatedCustomMessages = screenState.data.map {
                                    if (it.id == customMessage.id) customMessage.copy(isActive = !customMessage.isActive)
                                    else it
                                }
                                updatedCustomMessages
                            }
                            else -> mutableListOf()
                        }
                        _state.value = HomeScreenState(result = ScreenState.Success(updatedList))
                    }
                }
            }
        }
    }

    private fun deleteCustomMessage(id: Long) {
        viewModelScope.launch {
            deleteCustomMessageUseCase(id)
                .collectLatest { result ->
                    when(result){
                        is Resource.Error -> {
                        }
                        is Resource.Loading -> {
                            ScreenState.Loading
                        }
                        is Resource.Success -> {
                            val updatedList = when (val screenState = state.value.result) {
                                is ScreenState.Success -> {
                                    screenState.data.toMutableList()
                                }
                                else -> {
                                    mutableListOf()
                                }
                            }
                            updatedList.removeAll { it.id == id }
                            _state.value = HomeScreenState(
                                result = ScreenState.Success(updatedList)
                            )
                        }
                    }
                }
        }
    }

    fun getAllCustomMessages() {
        viewModelScope.launch {
            getAllCustomMessagesUseCase().collectLatest { result ->
                _state.value = HomeScreenState(
                    result = when (result) {
                        is Resource.Error -> ScreenState.Error(result.message!!)
                        is Resource.Loading -> ScreenState.Loading
                        is Resource.Success -> {
                            if (result.data!!.isEmpty()){
                                ScreenState.Error("No data found")
                            }
                            else{
                                ScreenState.Success(result.data)
                            }
                        }
                    }
                )
            }
        }
    }
}