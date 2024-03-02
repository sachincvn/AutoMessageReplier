package com.chavan.automessagereplier.presentation

sealed class Screen(
    val route : String
) {
    object TodoItemListScreen : Screen("todoItemList_screen")
    object TodoUpsertScreen : Screen("todoUpsert_screen")
}