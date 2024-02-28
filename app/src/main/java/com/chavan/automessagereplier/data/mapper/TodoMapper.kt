package com.chavan.automessagereplier.data.mapper

import com.chavan.automessagereplier.data.local.dto.LocalTodoItem
import com.chavan.automessagereplier.data.remote.dto.RemoteTodoItem
import com.chavan.automessagereplier.domain.model.TodoItem

fun TodoItem.toLocalTodoItem() : LocalTodoItem{
    return LocalTodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun TodoItem.toRemoteTodoItem() : RemoteTodoItem{
    return RemoteTodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun LocalTodoItem.toToDoItem() : TodoItem{
    return TodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun LocalTodoItem.toRemoteTodoItem() : RemoteTodoItem{
    return RemoteTodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun RemoteTodoItem.toTodoItem() : TodoItem{
    return TodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun RemoteTodoItem.toLocalTodoItem() : LocalTodoItem{
    return LocalTodoItem(
        title=title,
        description=description,
        timStamp=timStamp,
        isCompleted=isCompleted,
        isArchived=isArchived,
        id=id
    )
}

fun List<TodoItem>.toLocalTodoItemList() : List<LocalTodoItem>{
    return  this.map {todo ->
        LocalTodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}

fun List<TodoItem>.toRemoteTodoItemList() : List<RemoteTodoItem>{
    return  this.map {todo ->
        RemoteTodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}

fun List<LocalTodoItem>.toTodoItemListFromLocal() : List<TodoItem>{
    return  this.map {todo ->
        TodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}

fun List<LocalTodoItem>.toRemoteTodoItemListFromLocal() : List<RemoteTodoItem>{
    return  this.map {todo ->
        RemoteTodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}

fun List<RemoteTodoItem>.toTodoItemListFromRemote() : List<TodoItem>{
    return  this.map {todo ->
        TodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}

fun List<RemoteTodoItem>.toLocalTodoItemListFromRemote() : List<LocalTodoItem>{
    return  this.map {todo ->
        LocalTodoItem(
            title=todo.title,
            description=todo.description,
            timStamp=todo.timStamp,
            isCompleted=todo.isCompleted,
            isArchived=todo.isArchived,
            id=todo.id
        )
    }
}