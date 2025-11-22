package com.example.logtalk.core.utils.model

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlin.time.Duration.Companion.seconds



class OpenAILLMChatService(private val apiKey: String) {

    private val client = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 60.seconds)
    )

    // override 제거
    suspend fun getUserResponse(prompt: String): String {
        val request = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            ),
            maxTokens = 2000
        )

        val response = client.chatCompletion(request)

        return response.choices.firstOrNull()?.message?.content ?: ""
    }

}
