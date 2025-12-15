package com.example.logtalk.di

import android.content.Context
import com.example.logtalk.data.AppDatabase
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context) // 기존 DB 연결 로직 사용
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideTitleDao(database: AppDatabase): TitleDao {
        return database.titleDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}