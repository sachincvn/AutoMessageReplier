package com.chavan.automessagereplier.di

import android.app.Application
import androidx.room.Room
import com.chavan.automessagereplier.core.commons.Constants
import com.chavan.automessagereplier.data.local.CustomMessageDatabase
import com.chavan.automessagereplier.data.local.open_ai.OpenAiConfigDao
import com.chavan.automessagereplier.data.remote.services.openapi.IOpenaiGptApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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

    @Provides
    @Singleton
    fun provideOpenAiApi(): IOpenaiGptApi {
        return Retrofit.Builder()
            .baseUrl(Constants.openApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideOpenAiConfigDao(database: CustomMessageDatabase): OpenAiConfigDao {
        return database.openAiConfigDao
    }
}