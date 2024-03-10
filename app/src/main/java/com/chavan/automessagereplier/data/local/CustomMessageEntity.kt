package com.chavan.automessagereplier.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomMessageEntity(
    val receivedMessage : String,
    val replyMessage : String,
    val isActive : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0
)
