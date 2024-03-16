package com.chavan.automessagereplier.data.repository

import com.chavan.automessagereplier.data.local.CustomMessageDatabase
import com.chavan.automessagereplier.data.mapper.toCustomMessage
import com.chavan.automessagereplier.data.mapper.toCustomMessageEntity
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessageConfig
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomMessageRepoImpl @Inject constructor(
    private val db: CustomMessageDatabase,
) : CustomMessageRepo {

    private val dao = db.customMessageDao;
    override suspend fun getCustomMessages(): List<CustomMessage> {
        return dao.getCustomMessages().map {
            it.toCustomMessage()
        }
    }

    override suspend fun upsertCustomMessage(customMessage: CustomMessage) {
        return dao.upsertCustomMessage(customMessage.toCustomMessageEntity())
    }

    override suspend fun getCustomMessage(id: Long): CustomMessage? {
        return  dao.getCustomMessageById(id)?.toCustomMessage()
    }

    override suspend fun removeCustomMessage(id: Long) {
        return dao.removeCustomMessage(id)
    }

    override suspend fun configCustomMessage(customMessageConfig: CustomMessageConfig) {
        return dao.configCustomMessenger(customMessageConfig.toCustomMessageEntity())
    }

    override suspend fun getCustomMessageConfig(): CustomMessageConfig? {
        return dao.getCustomMessengerConfig()?.toCustomMessage()
    }
}