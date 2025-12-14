package com.example.logtalk.core.utils.model

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
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

class OpenIllegitimateSummarize(private val apiKey: String, private val firstMessage: String): OpenaiLLM {

    private val client = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 60.seconds)
    )
    private val titleMessage: MutableList<ChatMessage> = mutableListOf()
    init {
        // 1. 요약 작업을 지시하는 시스템 프롬프트 (혹은 사용자 메시지)
        titleMessage.add(
            ChatMessage(
                role = ChatRole.System, // 혹은 ChatRole.User
                content = "당신은 주어진 내용을 바탕으로 대화 주제를 간결하게 요약하는 전문 요약가입니다."
            )
        )

        titleMessage.add(
            ChatMessage(
                role = ChatRole.User,
                content = "다음 내용을 바탕으로 대화 주제가 무엇인지 간략하게 요약해줘:\n$firstMessage"
            )
        )
    }
    override suspend fun getResponse(prompt: String): String {
        val request = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            // ✨ 3. 저장된 전체 히스토리 리스트를 messages로 전송
            messages = titleMessage,
            maxTokens = 2000
        )

        val response = client.chatCompletion(request)

        return response.choices.firstOrNull()?.message?.content ?: ""
    }

}