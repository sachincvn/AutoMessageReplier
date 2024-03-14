package com.chavan.automessagereplier.di

import com.chavan.automessagereplier.data.repository.CustomMessageRepoImpl
import com.chavan.automessagereplier.data.repository.openai.OpenAiRepoImpl
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomMessageRepo(
        customMessageRepoImpl: CustomMessageRepoImpl
    ): CustomMessageRepo

    @Binds
    @Singleton
    abstract fun bindOpenAiApiRepo(
        openAiRepoImpl: OpenAiRepoImpl
    ): OpenAiApiRepo
}