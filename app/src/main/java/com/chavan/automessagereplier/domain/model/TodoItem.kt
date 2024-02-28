package com.chavan.automessagereplier.domain.model

import com.google.gson.annotations.SerializedName

data class TodoItem(
    val title: String,
    val description: String,
    @SerializedName("Timestamp")
    val timStamp: Long,
    val isCompleted: Boolean,
    val isArchived: Boolean,
    val id: Int
)
