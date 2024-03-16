package com.chavan.automessagereplier.domain.repository

import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessageConfig

interface CustomMessageRepo {
    suspend fun getCustomMessages(): List<CustomMessage>
    suspend fun upsertCustomMessage(customMessage: CustomMessage)
    suspend fun getCustomMessage(id: Long) : CustomMessage?
    suspend fun removeCustomMessage(id: Long)
    suspend fun configCustomMessage(customMessageConfig: CustomMessageConfig)
    suspend fun getCustomMessageConfig(): CustomMessageConfig?
}