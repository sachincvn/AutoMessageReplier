package com.chavan.automessagereplier.data.di

import android.content.Context
import androidx.room.Room
import com.chavan.automessagereplier.data.local.TodoDatabase
import com.chavan.automessagereplier.data.local.TodoDao
import com.chavan.automessagereplier.data.remote.TodoApi
import com.chavan.automessagereplier.data.repo.TodoListRepoIml
import com.chavan.automessagereplier.domain.repo.TodoListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit{
        return  Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://automessagereplier-default-rtdb.firebaseio.com/")
            .build()
    }

    @Provides
    fun providesRetrofitApi(retrofit: Retrofit) : TodoApi{
        return retrofit.create(TodoApi::class.java)
    }

    @Provides
    fun providesRoomDao(database: TodoDatabase) : TodoDao{
        return  database.dao
    }

    @Singleton
    @Provides
    fun providesRoomDb(
        @ApplicationContext appContext: Context
    ): TodoDatabase{
        return Room.databaseBuilder(
            appContext.applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesTodoRepo(
        db: TodoDatabase,
        api: TodoApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : TodoListRepo {
        return TodoListRepoIml(db.dao,api,dispatcher)
    }

}