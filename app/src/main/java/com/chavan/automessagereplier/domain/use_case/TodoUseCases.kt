package com.chavan.automessagereplier.domain.use_case

import com.chavan.automessagereplier.core.common.StringResources
import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.domain.repo.TodoListRepo
import com.chavan.automessagereplier.domain.util.InvalidTodoItemException
import com.chavan.automessagereplier.domain.util.SortingDirection
import com.chavan.automessagereplier.domain.util.TodoItemOrder
import javax.inject.Inject

class TodoUseCases @Inject constructor(
    private val todoListRepo: TodoListRepo
) {
   suspend fun addTodoItem(todoItem: TodoItem){
       if (todoItem.title.isEmpty() || todoItem.description.isEmpty()){
           throw InvalidTodoItemException(StringResources.invalidData)
       }
       todoListRepo.addTodoItem(todoItem)
   }

    suspend fun updateTodoItem(todoItem: TodoItem){
        if (todoItem.title.isEmpty() || todoItem.description.isEmpty()){
            throw InvalidTodoItemException(StringResources.invalidData)
        }
        todoListRepo.updateTodoItem(todoItem)
    }

    suspend fun deleteTodoItem(todoItem: TodoItem){
        todoListRepo.deleteTodoItem(todoItem)
    }

    suspend fun toggleCompleteTodoItem(todoItem: TodoItem){
        todoListRepo.updateTodoItem(todoItem.copy(isCompleted = !todoItem.isCompleted))
    }

    suspend fun toggleArchiveTodoItem(todoItem: TodoItem){
        todoListRepo.updateTodoItem(todoItem.copy(isArchived = !todoItem.isArchived))
    }

    suspend fun getTodoItemById(id: Int) : TodoItem?{
        return todoListRepo.getTodoById(id)
    }

    suspend fun getTodoItems(
        todoItemOrder: TodoItemOrder = TodoItemOrder.Time(SortingDirection.Down,true)
    ) : TodoUseCaseResult {
        var todos = todoListRepo.getAllTodoFromLocalCache()
        if (todos.isEmpty()){
            todos = todoListRepo.getAllTodo()
        }

        val filteredTodos = if (todoItemOrder.showArchived){
            todos
        }
        else{
            todos.filter { !it.isArchived }
        }

        return when(todoItemOrder.sortingDirection){
            is SortingDirection.Down -> {
                when(todoItemOrder){
                    is TodoItemOrder.Title -> TodoUseCaseResult.Success(
                        filteredTodos.sortedByDescending { it.title.lowercase() }
                    )

                    is TodoItemOrder.Time -> TodoUseCaseResult.Success(
                        filteredTodos.sortedByDescending { it.timStamp }
                    )

                    is TodoItemOrder.Completed -> TodoUseCaseResult.Success(
                        filteredTodos.sortedByDescending { it.isCompleted }
                    )
                }
            }
            is  SortingDirection.Up -> {
                when(todoItemOrder){
                    is TodoItemOrder.Title -> TodoUseCaseResult.Success(
                        filteredTodos.sortedBy { it.title.lowercase() }
                    )

                    is TodoItemOrder.Time -> TodoUseCaseResult.Success(
                        filteredTodos.sortedBy { it.timStamp }
                    )

                    is TodoItemOrder.Completed -> TodoUseCaseResult.Success(
                        filteredTodos.sortedBy { it.isCompleted }
                    )
                }
            }
        }
    }
}

sealed class TodoUseCaseResult{
    data class Success(val todoItems: List<TodoItem>) : TodoUseCaseResult()
    data class Error(val message: String) : TodoUseCaseResult()

}