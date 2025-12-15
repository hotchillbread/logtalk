package com.example.logtalk.di

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.domain.chat.ChatRepositoryImpl
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.core.utils.model.OpenIllegitimateSummarize
import com.example.logtalk.core.utils.SystemPromptManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    companion object {

        // API Key 제공
        @Provides
        @Singleton
        fun provideApiKey(): String {
            return EnvManager.getOpenaiApiKey()
        }

        // LLM 서비스
        @Provides
        @Singleton
        fun provideOpenAILLMChatService(
            apiKey: String,
            systemPromptManager: SystemPromptManager
        ): OpenAILLMChatService {
            // SystemPromptManager의 현재 프롬프트를 초기값으로 사용
            val systemPrompt = systemPromptManager.getCurrentPrompt()
            return OpenAILLMChatService(apiKey, systemPrompt)
        }

        @Provides
        @Singleton
        fun provideOpenIllegitimateSummarize(apiKey: String): OpenIllegitimateSummarize {
            return OpenIllegitimateSummarize(apiKey, firstMessage = "")
        }
    }
}