package com.chavan.automessagereplier.presentation.todo_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.core.common.StringResources
import com.chavan.automessagereplier.data.di.IoDispatcher
import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.domain.use_case.TodoUseCaseResult
import com.chavan.automessagereplier.domain.use_case.TodoUseCases
import com.chavan.automessagereplier.domain.util.TodoItemOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = mutableStateOf(TodoListState())
    val state : State<TodoListState> = _state

    private var undoTodoItem : TodoItem? = null
    private var getTodoItemsJob : Job? = null
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    fun onEvent(todoListEvent: TodoListEvent){
        when(todoListEvent){
            is TodoListEvent.Delete -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    todoUseCases.deleteTodoItem(todoListEvent.todoItem)
                    getTodoItems()
                    undoTodoItem = todoListEvent.todoItem
                }
            }
            is TodoListEvent.Sort -> {
                val isStateChanged = !( _state.value.todoItemOrder::class == todoListEvent.todoItemOrder::class &&
                        _state.value.todoItemOrder.showArchived == todoListEvent.todoItemOrder.showArchived &&
                        _state.value.todoItemOrder.sortingDirection == todoListEvent.todoItemOrder.sortingDirection )
                if (isStateChanged)
                    return

                _state.value = _state.value.copy(
                    todoItemOrder = todoListEvent.todoItemOrder
                )
                getTodoItems()
            }
            is TodoListEvent.ToggleArchived -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    todoUseCases.toggleArchiveTodoItem(todoItem = todoListEvent.todoItem)
                    getTodoItems()
                }
            }
            is TodoListEvent.ToggleCompleted -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    todoUseCases.toggleCompleteTodoItem(todoItem = todoListEvent.todoItem)
                    getTodoItems()
                }
            }
            TodoListEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    todoUseCases.addTodoItem(todoItem = undoTodoItem ?: return@launch)
                    undoTodoItem = null
                    getTodoItems()
                }
            }
        }
    }

   fun getTodoItems(){
       getTodoItemsJob?.cancel()

       getTodoItemsJob = viewModelScope.launch(dispatcher+errorHandler){
           val result = todoUseCases.getTodoItems(
               todoItemOrder = _state.value.todoItemOrder
           )

           when(result){
               is TodoUseCaseResult.Success -> {
                   _state.value = state.value.copy(
                       todoItems = result.todoItems,
                       todoItemOrder = _state.value.todoItemOrder,
                       isLoading = false
                   )
               }
               is TodoUseCaseResult.Error -> {
                   _state.value = state.value.copy(
                       error = StringResources.todoNotFound,
                       isLoading = false
                   )
               }
           }
       }
   }
}