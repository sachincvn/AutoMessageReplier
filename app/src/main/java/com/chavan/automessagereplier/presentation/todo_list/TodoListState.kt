package com.chavan.automessagereplier.presentation.todo_list

import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.domain.util.SortingDirection
import com.chavan.automessagereplier.domain.util.TodoItemOrder

data class TodoListState(
    val todoItems : List<TodoItem> = emptyList(),
    val todoItemOrder : TodoItemOrder = TodoItemOrder.Time(SortingDirection.Down,true),
    val isLoading : Boolean = true,
    val error : String? = null
)