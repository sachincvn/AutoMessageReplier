package com.chavan.automessagereplier.presentation.todo_list.components

import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import com.chavan.automessagereplier.core.common.StringResources
import com.chavan.automessagereplier.domain.util.SortingDirection
import com.chavan.automessagereplier.domain.util.TodoItemOrder

@Composable
fun SortingDrawerMenu(
    todoItemOrder: TodoItemOrder,
    onOrderChange : (TodoItemOrder)-> Unit,
) {
    val titleSelected = todoItemOrder::class == TodoItemOrder.Title::class
    NavigationDrawerItem(
        label = { 
                IconRow(text = StringResources.TITLE, isChecked = titleSelected)
                },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Title(todoItemOrder.sortingDirection,todoItemOrder.showArchived))
        }
    )

    val timeSelected = todoItemOrder::class == TodoItemOrder.Time::class
    NavigationDrawerItem(
        label = {
            IconRow(
                text = StringResources.time,
                isChecked = timeSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Time(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    val completedSelected = todoItemOrder::class == TodoItemOrder.Completed::class
    NavigationDrawerItem(
        label = {
            IconRow(
                text = StringResources.completed,
                isChecked = completedSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Completed(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    Divider()

    val sortDownSelected = todoItemOrder.sortingDirection == SortingDirection.Down
    NavigationDrawerItem(
        label = {
            IconRow(
                text = StringResources.sortDown,
                isChecked = sortDownSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.Down, todoItemOrder.showArchived))
        }
    )

    val sortUpSelected = todoItemOrder.sortingDirection == SortingDirection.Up
    NavigationDrawerItem(
        label = {
            IconRow(
                text = StringResources.sortUp,
                isChecked = sortUpSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.Up, todoItemOrder.showArchived))
        }
    )

    Divider()

    NavigationDrawerItem(
        label = {
            IconRow(text = StringResources.showArchived, isChecked = todoItemOrder.showArchived)
        }, selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(todoItemOrder.sortingDirection, !todoItemOrder.showArchived))
        })
}