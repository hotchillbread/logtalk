package com.example.logtalk.di

import android.content.Context
import com.example.logtalk.config.EnvManager
import com.example.logtalk.data.AppDatabase
import com.example.logtalk.data.local.ChatDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.UserDao

import com.example.logtalk.data.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // 앱 수명주기 동안 유지
object AppModule {

    // 1. Room Database 제공
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    // 2. DAO 제공 (Repository에서 쓰기 위함)
    @Provides
    fun provideMessageDao(database: AppDatabase): ChatDao = database.messageDao()

    @Provides
    fun provideTitleDao(database: AppDatabase): TitleDao = database.titleDao()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    // 3. API Key 제공 (EnvManager를 통해)
    @Provides
    @Singleton
    fun provideApiKey(): String {
        // EnvManager에서 키를 가져오는 메서드가 있다고 가정 (없으면 EnvManager.apiKey 등으로 접근)
        return EnvManager.getOpenaiApiKey() ?: ""
    }
}