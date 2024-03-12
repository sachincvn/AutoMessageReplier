package com.chavan.automessagereplier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chavan.automessagereplier.core.utils.Converters

@Database(
    entities = [
        CustomMessageEntity::class,
        CustomMessageConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CustomMessageDatabase : RoomDatabase() {
    abstract val dao: CustomMessageDao
}
