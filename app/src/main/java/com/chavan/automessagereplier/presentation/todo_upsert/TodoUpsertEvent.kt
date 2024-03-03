package com.chavan.automessagereplier.presentation.todo_upsert

import androidx.compose.ui.focus.FocusState

sealed class TodoUpsertEvent {
    data class EnteredTitle(val value: String) : TodoUpsertEvent()
    data class ChangedTitleFocus(val focusState: FocusState) : TodoUpsertEvent()
    data class EnteredDescription(val value: String) : TodoUpsertEvent()
    data class ChangedDescriptionFocus(val focusState: FocusState) : TodoUpsertEvent()

    object Delete : TodoUpsertEvent()
    object ToggleCompleted : TodoUpsertEvent()
    object ToggleArchived : TodoUpsertEvent()
    object SaveTodo : TodoUpsertEvent()
    object Back : TodoUpsertEvent()

}