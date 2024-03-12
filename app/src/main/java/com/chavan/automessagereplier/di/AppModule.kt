package com.chavan.automessagereplier.di

import android.app.Application
import androidx.room.Room
import com.chavan.automessagereplier.data.local.CustomMessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesCustomMessageDatabase(app: Application): CustomMessageDatabase {
        return Room.databaseBuilder(
            app,
            CustomMessageDatabase::class.java,
            "custom_message_db.db"
        ).build()
    }
}