package com.example.logtalk.core.utils.model

import com.example.logtalk.config.EnvManager



class DependencyContainer(private val envManager: EnvManager) {
    private val openAiApiKey: String by lazy {

        envManager.getOpenaiApiKey()
    }

    private val userProfilePrompt: String = "여기다가 로직 넣어야돼요"

    // LLM
    val llmService: OpenAILLMChatService by lazy {
        OpenAILLMChatService(apiKey = openAiApiKey, systemPrompt = userProfilePrompt)
    }
    // STT
    val sttService: OpenAISttService by lazy {
        OpenAISttService(apiKey = openAiApiKey)
    }

    // Embedding
    val embeddingService: OpenAIEmbeddingService by lazy {
        OpenAIEmbeddingService(apiKey = openAiApiKey)
    }
}