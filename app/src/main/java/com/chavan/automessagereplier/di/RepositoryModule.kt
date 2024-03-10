package com.chavan.automessagereplier.di

import com.chavan.automessagereplier.data.repository.CustomMessageRepoImpl
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
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
    ) : CustomMessageRepo
}