package com.chavan.automessagereplier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chavan.automessagereplier.core.utils.Converters
import com.chavan.automessagereplier.data.local.custom_message.CustomMessageConfigEntity
import com.chavan.automessagereplier.data.local.custom_message.CustomMessageDao
import com.chavan.automessagereplier.data.local.custom_message.CustomMessageEntity
import com.chavan.automessagereplier.data.local.open_ai.OpenAiConfigDao
import com.chavan.automessagereplier.data.local.open_ai.OpenAiConfigEntity

@Database(
    entities = [
        CustomMessageEntity::class,
        CustomMessageConfigEntity::class,
        OpenAiConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CustomMessageDatabase : RoomDatabase() {
    abstract val customMessageDao: CustomMessageDao
    abstract val openAiConfigDao: OpenAiConfigDao
}
