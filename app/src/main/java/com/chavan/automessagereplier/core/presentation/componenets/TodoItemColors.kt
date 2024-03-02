package com.chavan.automessagereplier.core.presentation.componenets

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.chavan.automessagereplier.domain.model.TodoItem

data class TodoItemColors(
    val backgroundColor : Color,
    val textColor : Color,
    val archiveIconColor : Color,
    val checkColor : Color
)
@Composable
fun getTodoColors(todoItem: TodoItem): TodoItemColors {
    return if(todoItem.isArchived){
        TodoItemColors(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
            textColor = MaterialTheme.colorScheme.onSecondary,
            archiveIconColor = MaterialTheme.colorScheme.onSecondary,
            checkColor = if(todoItem.isCompleted) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.onSecondary
        )
    }else{
        TodoItemColors(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            archiveIconColor = MaterialTheme.colorScheme.secondary,
            checkColor = if(todoItem.isCompleted) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.secondary
        )
    }
}