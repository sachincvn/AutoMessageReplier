package com.chavan.automessagereplier.domain.repo

import com.chavan.automessagereplier.domain.model.TodoItem

interface TodoListRepo {
    suspend fun getAllTodo() : List<TodoItem>
    suspend fun getAllTodoFromLocalCache() : List<TodoItem>
    suspend fun getAllTodoFromRemote()
    suspend fun getTodoById(id:Int) : TodoItem?
    suspend fun addTodoItem(todo:TodoItem)
    suspend fun updateTodoItem(todo: TodoItem)
    suspend fun deleteTodoItem(todo: TodoItem)
}