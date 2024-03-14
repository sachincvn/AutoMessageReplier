package com.chavan.automessagereplier.data.local.open_ai

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface OpenAiConfigDao {
    @Upsert
    suspend fun upsertOpenAiConfig(openAiConfigEntity: OpenAiConfigEntity)
    @Query("SELECT * FROM openaiconfigentity")
    suspend fun getOpenAiConfig(): OpenAiConfigEntity
}