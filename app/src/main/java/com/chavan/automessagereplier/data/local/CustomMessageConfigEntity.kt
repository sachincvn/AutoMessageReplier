package com.chavan.automessagereplier.data.local

import androidx.room.Entity

@Entity
data class CustomMessageConfigEntity(
    val isActive : Boolean = true,
    val chatGptApiKey : String? = null,
)