package com.chavan.automessagereplier.data.repository

import com.chavan.automessagereplier.data.local.CustomMessageDatabase
import com.chavan.automessagereplier.data.mapper.toCustomMessage
import com.chavan.automessagereplier.data.mapper.toCustomMessageEntity
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.model.CustomMessageConfig
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomMessageRepoImpl @Inject constructor(
    private val db: CustomMessageDatabase,
) : CustomMessageRepo {

    private val dao = db.dao;
    override suspend fun getCustomMessages(): List<CustomMessage> {
        delay(5000)
        return dao.getCustomMessages().map {
            it.toCustomMessage()
        }
    }

    override suspend fun upsertCustomMessage(customMessage: CustomMessage) {
        return dao.upsertCustomMessage(customMessage.toCustomMessageEntity())
    }

    override suspend fun removeCustomMessage(id: Long) {
        return dao.removeCustomMessage(id)
    }

    override suspend fun configCustomMessage(customMessageConfig: CustomMessageConfig) {
        return dao.configCustomMessenger(customMessageConfig.toCustomMessageEntity())
    }

    override suspend fun getCustomMessageConfig(): CustomMessageConfig {
        return dao.getCustomMessengerConfig()!!.toCustomMessage()
    }
}