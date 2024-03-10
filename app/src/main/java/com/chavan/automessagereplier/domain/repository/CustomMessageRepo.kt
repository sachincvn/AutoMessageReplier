package com.chavan.automessagereplier.domain.repository

import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.model.CustomMessage
import kotlinx.coroutines.flow.Flow

interface CustomMessageRepo {
    suspend fun getCustomMessages() : List<CustomMessage>
    suspend fun upsertCustomMessage(customMessage: CustomMessage)
    suspend fun removeCustomMessage(id: Long)

}