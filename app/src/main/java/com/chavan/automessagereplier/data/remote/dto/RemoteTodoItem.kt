package com.chavan.automessagereplier.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RemoteTodoItem(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Timestamp")
    val timStamp: Long,
    @SerializedName("IsCompleted")
    val isCompleted: Boolean,
    @SerializedName("IsArchived")
    val isArchived: Boolean,
    @SerializedName("Id")
    val id: Int
)
