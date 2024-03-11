package com.chavan.automessagereplier.domain.repository

import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.model.CustomMessageConfig
import kotlinx.coroutines.flow.Flow

interface CustomMessageRepo {
    suspend fun getCustomMessages() : List<CustomMessage>
    suspend fun upsertCustomMessage(customMessage: CustomMessage)
    suspend fun removeCustomMessage(id: Long)
    suspend fun configCustomMessage(customMessageConfig: CustomMessageConfig)

}