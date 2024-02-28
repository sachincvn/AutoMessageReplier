package com.chavan.automessagereplier.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RemoteTodoItem(
    val title: String,
    val description: String,
    @SerializedName("Timestamp")
    val timStamp: Long,
    val isCompleted: Boolean,
    val isArchived: Boolean,
    val id: Int
)
