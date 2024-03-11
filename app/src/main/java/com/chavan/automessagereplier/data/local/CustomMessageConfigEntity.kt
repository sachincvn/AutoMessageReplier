package com.chavan.automessagereplier.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomMessageConfigEntity(
    val isActive : Boolean = true,
    val chatGptApiKey : String? = null,
    @PrimaryKey
    val id : Int = 0
)