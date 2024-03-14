package com.chavan.automessagereplier.data.local.custom_message

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CustomMessageDao {
    @Upsert
    suspend fun upsertCustomMessage(customMessageEntity: CustomMessageEntity)

    @Query("DELETE FROM custommessageentity WHERE id=:id")
    suspend fun removeCustomMessage(id: Long)

    @Query("SELECT * FROM custommessageentity")
    suspend fun getCustomMessages(): List<CustomMessageEntity>

    @Upsert
    suspend fun configCustomMessenger(customMessageConfigEntity: CustomMessageConfigEntity)

    @Query("SELECT * FROM custommessageconfigentity")
    suspend fun getCustomMessengerConfig(): CustomMessageConfigEntity?
}