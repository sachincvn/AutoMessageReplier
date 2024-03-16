package com.chavan.automessagereplier.data.local.custom_message

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CustomMessageDao {
    @Upsert
    suspend fun upsertCustomMessage(customMessageEntity: CustomMessageEntity)

    @Query("SELECT * FROM custommessageentity WHERE id=:id")
    suspend fun getCustomMessageById(id: Long) : CustomMessageEntity?
    @Query("DELETE FROM custommessageentity WHERE id=:id")
    suspend fun removeCustomMessage(id: Long)

    @Query("SELECT * FROM custommessageentity ORDER BY lastUpdated DESC")
    suspend fun getCustomMessages(): List<CustomMessageEntity>
    @Upsert
    suspend fun configCustomMessenger(customMessageConfigEntity: CustomMessageConfigEntity)

    @Query("SELECT * FROM custommessageconfigentity")
    suspend fun getCustomMessengerConfig(): CustomMessageConfigEntity?
}