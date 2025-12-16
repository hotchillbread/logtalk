package com.example.logtalk.di

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.domain.chat.ChatRepositoryImpl
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.EmbeddingService
import com.example.logtalk.core.utils.model.OpenAIEmbeddingService
import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.core.utils.model.OpenIllegitimateSummarize
import com.example.logtalk.data.repository.SessionRepositoryImpl
import com.example.logtalk.domain.relatedChat.FindRelatedConsultationsUseCase
import com.example.logtalk.domain.relatedChat.FindRelatedConsultationsUseCaseImpl
import com.example.logtalk.domain.relatedChat.RelatedChatRepository
import com.example.logtalk.domain.relatedChat.RelatedChatRepositoryImpl
import com.example.logtalk.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule { // ✨ @Binds를 사용하므로 abstract로 변경

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindRelatedChatRepository(
        impl: RelatedChatRepositoryImpl
    ): RelatedChatRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindFindRelatedConsultationsUseCase(
        impl: FindRelatedConsultationsUseCaseImpl
    ): FindRelatedConsultationsUseCase

    @Binds
    @Singleton
    abstract fun bindEmbeddingService(
        impl: OpenAIEmbeddingService
    ): EmbeddingService

    companion object {

        // API Key 제공
        @Provides
        @Singleton
        fun provideApiKey(): String {
            return EnvManager.getOpenaiApiKey()
        }

        // embedding 서비스
        @Provides
        @Singleton
        fun provideOpenAIEmbeddingService(apiKey: String): OpenAIEmbeddingService {
            return OpenAIEmbeddingService(apiKey)
        }

        // LLM 서비스
        @Provides
        @Singleton
        fun provideOpenAILLMChatService(apiKey: String): OpenAILLMChatService {
            val systemPrompt = "사용자에게 공감하고 문제 해결을 돕는 전문가 상담 챗봇입니다."
            return OpenAILLMChatService(apiKey, systemPrompt)
        }

        @Provides
        @Singleton
        fun provideOpenIllegitimateSummarize(apiKey: String): OpenIllegitimateSummarize {
            return OpenIllegitimateSummarize(apiKey, firstMessage = "")
        }


    }
}