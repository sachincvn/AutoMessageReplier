package com.chavan.automessagereplier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CustomMessageEntity::class,
    ],
    version = 1
)
abstract class CustomMessageDatabase : RoomDatabase() {
        abstract val dao: CustomMessageDao
}
