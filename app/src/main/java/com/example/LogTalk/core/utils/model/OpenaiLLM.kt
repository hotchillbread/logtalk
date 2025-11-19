package com.example.logtalk.core.utils.model


interface LLMService {
    suspend fun GetUserResponse(prompt: String): String
}

//채팅에 사용할 LLM
class OpenAILLMChatService(private val apiKey: String): LLMService {
    // OpenAI SDK (Retrofit 기반 등)를 사용하여 클라이언트 설정
    private val openAIClient: OpenAI = OpenAI(
        // API 키를 주입받아 사용
        token = apiKey
    )

    override suspend fun getLlmResponse(prompt: String): String {
        // 실제 API 호출 로직
        // 예: chat.completions API 사용
        val completion = openAIClient.chat.completions.create(
            request = ChatCompletionRequest(
                model = ModelId("gpt-4o-mini"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = prompt
                    )
                )
            )
        )
        return completion.choices.first().message.content ?: ""
    }
}
