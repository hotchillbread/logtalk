package com.example.logtalk.di

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.domain.chat.ChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {


    @Provides
    @Singleton
    fun provideChatUseCase(
        repository: ChatRepository
    ): ChatUseCase {
        return ChatUseCase(repository = repository)
    }
}

