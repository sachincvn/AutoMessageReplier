package com.chavan.automessagereplier.presentation.todo_list

import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.domain.util.TodoItemOrder

sealed class TodoListEvent {
    data class Sort(val todoItemOrder: TodoItemOrder) : TodoListEvent()
    data class Delete(val todoItem: TodoItem) : TodoListEvent()
    data class ToggleCompleted(val todoItem: TodoItem) : TodoListEvent()
    data class ToggleArchived(val todoItem: TodoItem) : TodoListEvent()
    object UndoDelete : TodoListEvent()
}