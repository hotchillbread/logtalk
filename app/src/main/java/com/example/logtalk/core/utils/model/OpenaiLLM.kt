package com.example.logtalk.core.utils.model

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.logtalk.core.utils.Logger
import kotlin.time.Duration.Companion.seconds

//인터페이스
interface OpenaiLLM {
    suspend fun getResponse(prompt: String): String
}


//기본 쳇봇
class OpenAILLMChatService(
    private val apiKey: String,
    private val systemPrompt: String,
    private val maxHistoryMessages: Int = 12 //디폴트 벨류 설정
): OpenaiLLM {
    init { Logger.d("OpenAI Client Init - Received Key: $apiKey") }
    private val client = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 60.seconds)
    )

    private val chatHistory: MutableList<ChatMessage> = mutableListOf()
    //기록 짤릴경우 요약 내용으로 대체
    private var summaryMessage: ChatMessage? = null


    init {
        resetHistory()
    }

    public fun resetHistory() {
        chatHistory.clear()
        chatHistory.add(
            ChatMessage(
                role = ChatRole.System,
                content = systemPrompt
            )
        )

        summaryMessage = null
    }

    private suspend fun eliminateHistory() {
        if (chatHistory.size > maxHistoryMessages) {
            //시스템 프롬프트 제외한 메세지 내역 추출
            val messagesToSummarize = chatHistory.drop(1)

            val summaryText = summaryMessages(messagesToSummarize)

            //이전 대화 요약을 위한 변수 값 할당
            summaryMessage = ChatMessage(
                role = ChatRole.System,
                content = "Previous summary:\n${summaryMessage?.content ?: ""}\nNew summary:\n$summaryText"
            )

            chatHistory.retainAll(listOf(chatHistory.first()))
            summaryMessage?.let { chatHistory.add(it) }
        }
    }

    private suspend fun summaryMessages(messages: List<ChatMessage>): String {
        val summaryRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = listOf(
                ChatMessage(ChatRole.System, "다음 대화를 잃지 않도록 간결하지만 중요한 내용은 빠짐없이 요약해."),
                *messages.toTypedArray()
            ),
            maxTokens = 200
        )

        val summaryResponse = client.chatCompletion(summaryRequest)
        return summaryResponse.choices.firstOrNull()?.message?.content ?: "요약내용 없음"
    }

    override suspend fun getResponse(prompt: String): String {
        val userMessage = ChatMessage(
            role = ChatRole.User,
            content = prompt
        )
        chatHistory.add(userMessage)

        //히스트로 제거여부 검사
        eliminateHistory()


        val request = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            // ✨ 3. 저장된 전체 히스토리 리스트를 messages로 전송
            messages = chatHistory,
            maxTokens = 2000
        )

        val response = client.chatCompletion(request)

        val assistantContent = response.choices.firstOrNull()?.message?.content ?: ""

        // 4. 모델 응답을 ChatMessage로 만들어 히스토리에 추가 (다음 요청을 위해)
        if (assistantContent.isNotEmpty()) {
            val assistantMessage = ChatMessage(
                role = ChatRole.Assistant,
                content = assistantContent
            )
            chatHistory.add(assistantMessage)
        }

        return assistantContent
    }
}

class OpenIllegitimateSummarize(private val apiKey: String): OpenaiLLM { // 💡 firstMessage를 생성자에서 제거!

    private val client = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 60.seconds)
    )

    override suspend fun getResponse(prompt: String): String { // 💡 prompt 인자를 요약할 텍스트로 사용

        // 1. System Role로 명확한 지침을 전달합니다. (가장 중요)
        val systemMessage = ChatMessage(
            role = ChatRole.System,
            content = "You are an expert title generator. Your task is to summarize the following user's first message into a short, concise title for a conversation. The title must be in Korean and strictly contain only the title text."
        )

        // 2. User Role로 요약할 실제 메시지를 전달합니다.
        val userMessage = ChatMessage(
            role = ChatRole.User,
            content = prompt // 💡 getResponse의 인자로 받은 prompt를 요약 대상으로 사용
        )

        val messages = listOf(systemMessage, userMessage) // 3. List 구성

        val request = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = messages, // 4. 새로 구성된 List 사용
            maxTokens = 50 // 💡 제목이므로 maxTokens를 낮춰서 비용과 속도 최적화
        )

        val response = client.chatCompletion(request)

        return response.choices.firstOrNull()?.message?.content?.trim()?.replace("\"", "") ?: ""
    }

}