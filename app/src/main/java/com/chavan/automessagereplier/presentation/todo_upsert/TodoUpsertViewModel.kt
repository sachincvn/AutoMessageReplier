package com.chavan.automessagereplier.presentation.todo_upsert

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.data.di.IoDispatcher
import com.chavan.automessagereplier.domain.use_case.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoUpsertViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel(){
    private val _state = mutableStateOf(TodoUpsertState())
    val state : State<TodoUpsertState> = _state

    private var currentTodoId : Int? = null

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {
        savedStateHandle.get<Int>("todoId")?.let { id ->
            if (id  != -1){
             viewModelScope.launch(dispatcher+errorHandler) {
                 todoUseCases.getTodoItemById(id)?.also { todo->
                     currentTodoId = id
                     _state.value = _state.value.copy(
                         todoItem = todo,
                         isLoading = false,
                         isTitleHintVisible = todo.title.isBlank(),
                         isDescriptionHintVisible = todo.description.isBlank()
                     )
                 }
             }
            }
            else{
                _state.value = _state.value.copy(
                    isLoading = false,
                )
            }
        }
    }
    sealed class UIEvent{
        data class ShowSnackBar(val message : String) : UIEvent()
        object SaveTodo : UIEvent()
        object Back : UIEvent()
    }

    fun onEvent(event: TodoUpsertEvent){
        when(event){
            TodoUpsertEvent.Back -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    _eventFlow.emit(UIEvent.Back)
                }
            }
            is TodoUpsertEvent.ChangedDescriptionFocus -> {
                val shouldDescriptionHintBeVisible = !event.focusState.isFocused && _state.value.todoItem.description.isBlank()
                _state.value = _state.value.copy(
                    isDescriptionHintVisible = shouldDescriptionHintBeVisible
                )
            }
            is TodoUpsertEvent.ChangedTitleFocus -> {
                val shouldTitleHintBeVisible = !event.focusState.isFocused && _state.value.todoItem.title.isBlank()
                _state.value = _state.value.copy(
                    isDescriptionHintVisible = shouldTitleHintBeVisible
                )
            }
            TodoUpsertEvent.Delete -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    if (currentTodoId!=null){
                        todoUseCases.deleteTodoItem(_state.value.todoItem)
                    }
                    _eventFlow.emit(UIEvent.Back)
                }
            }
            is TodoUpsertEvent.EnteredDescription -> {
                _state.value = _state.value.copy(
                    todoItem = _state.value.todoItem.copy(
                        description = event.value
                    )
                )
            }
            is TodoUpsertEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    todoItem = _state.value.todoItem.copy(
                        title = event.value
                    )
                )
            }
            TodoUpsertEvent.SaveTodo -> {
                viewModelScope.launch (dispatcher+errorHandler){
                    try {
                        if (currentTodoId!=null){
                            todoUseCases.updateTodoItem(_state.value.todoItem)
                        }
                        else{
                            todoUseCases.addTodoItem(_state.value.todoItem.copy(
                                timStamp = System.currentTimeMillis(),
                                id = null,
                            ))
                        }
                        _eventFlow.emit(UIEvent.SaveTodo)
                    }
                    catch (ex : Exception){
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(
                                message = ex.message?: "Error While Saving Todo"
                            )
                        )
                    }
                }
            }
            TodoUpsertEvent.ToggleArchived -> {
             _state.value =  _state.value.copy(
                 todoItem = _state.value.todoItem.copy(
                     isArchived = !_state.value.todoItem.isArchived
                 )
             )
            }
            TodoUpsertEvent.ToggleCompleted -> {
                _state.value =  _state.value.copy(
                    todoItem = _state.value.todoItem.copy(
                        isCompleted = !_state.value.todoItem.isCompleted
                    )
                )
            }
        }
    }
}