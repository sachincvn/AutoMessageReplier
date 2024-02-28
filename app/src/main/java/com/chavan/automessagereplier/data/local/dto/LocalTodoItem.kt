package com.chavan.automessagereplier.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo")
data class LocalTodoItem(
    val title: String,
    val description: String,
    val timStamp: Long,
    val isCompleted: Boolean,
    val isArchived: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)