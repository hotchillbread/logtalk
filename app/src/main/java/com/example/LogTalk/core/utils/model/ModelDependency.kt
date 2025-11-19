package com.example.logtalk.core.utils.model

import com.example.logtalk.config.EnvManager

class DependencyContainer(private val envManager: EnvManager) {
    private val openAiApiKey: String by lazy {

        envManager.getOpenaiApiKey()
    }

    // LLM
    val llmService: OpenAILLMChatService by lazy {
        OpenAILLMChatService(apiKey = openAiApiKey)
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