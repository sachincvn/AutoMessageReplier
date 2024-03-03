package com.chavan.automessagereplier.presentation.todo_upsert

import com.chavan.automessagereplier.domain.model.TodoItem

data class TodoUpsertState(
    val isTitleHintVisible : Boolean = true,
    val isDescriptionHintVisible : Boolean =  true,
    val todoItem: TodoItem =  TodoItem(
        title = "",
        description = "",
        timStamp = 0,
        isCompleted = false,
        isArchived = false,
        id = null,
    ),
    val isLoading : Boolean =true,
    val error : String? = null,
)