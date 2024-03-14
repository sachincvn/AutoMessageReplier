package com.chavan.automessagereplier.data.local.custom_message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomMessageConfigEntity(
    val isActive : Boolean = true,
    val chatGptApiKey : String? = null,
    @PrimaryKey
    val id : Int = 0
)