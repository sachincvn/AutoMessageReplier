package com.chavan.automessagereplier.data.repository.openai

import com.chavan.automessagereplier.data.local.open_ai.OpenAiConfigDao
import com.chavan.automessagereplier.data.mapper.toOpenAiConfigDaoEntity
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import com.chavan.automessagereplier.data.remote.services.openapi.IOpenaiGptApi
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import javax.inject.Inject

class OpenAiRepoImpl @Inject constructor(
    private val api: IOpenaiGptApi,
    private val openAiConfigDao: OpenAiConfigDao
) : OpenAiApiRepo {

    override suspend fun getOpenAiResponse(chatRequestDTO: ChatRequestDTO): OpenAiGptDTO {
        return api.getOpenAiResponse(chatRequestDTO)
    }

    override suspend fun getOpenAiLocalConfig(): OpenAiConfig {
        return openAiConfigDao.getOpenAiConfig().toOpenAiConfigDaoEntity()
    }

    override suspend fun upsertOpenAiConfig(openAiConfig: OpenAiConfig) {
        return openAiConfigDao.upsertOpenAiConfig(openAiConfig.toOpenAiConfigDaoEntity())
    }
}