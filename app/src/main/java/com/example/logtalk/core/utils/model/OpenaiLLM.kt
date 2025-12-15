package com.example.logtalk.core.utils.model

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.logtalk.core.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

//인터페이스
interface OpenaiLLM {
    suspend fun getResponse(prompt: String): String
}


//기본 쳇봇
class OpenAILLMChatService(
    private val apiKey: String,
    private var systemPrompt: String,
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

    fun resetHistory() {
        chatHistory.clear()
        chatHistory.add(
            ChatMessage(
                role = ChatRole.System,
                content = systemPrompt
            )
        )

        summaryMessage = null
    }

    // 시스템 프롬프트를 런타임에 업데이트. 기본은 히스토리 유지 모드.
    fun updateSystemPrompt(newPrompt: String, preserveHistory: Boolean = true) {
        systemPrompt = newPrompt
        if (chatHistory.isEmpty()) {
            // 안전장치: 히스토리가 비어있으면 새 시스템 프롬프트 추가
            chatHistory.add(ChatMessage(ChatRole.System, systemPrompt))
            return
        }
        if (preserveHistory) {
            // 첫 메시지가 시스템이면 내용을 교체하여 UI에 노출 없이 즉시 반영
            val first = chatHistory.first()
            if (first.role == ChatRole.System) {
                chatHistory[0] = ChatMessage(ChatRole.System, systemPrompt)
            } else {
                // 예상치 못한 상태: 앞에 사용자/어시스턴트가 있을 경우 맨 앞에 시스템 프롬프트 삽입
                chatHistory.add(0, ChatMessage(ChatRole.System, systemPrompt))
            }
            // 요약 메시지는 유지되며 다음 요청부터 그대로 활용
        } else {
            // 히스토리 초기화 모드: 새 시스템 프롬프트로 히스토리를 재설정
            resetHistory()
        }
    }

    private suspend fun eliminateHistory() {
        if (chatHistory.size > maxHistoryMessages) {
            // 시스템 프롬프트는 항상 첫 번째 메시지
            val systemPromptMessage = chatHistory.first()

            // 시스템 프롬프트와 기존 요약 메시지를 제외한 실제 대화 내역만 추출
            val startIndex = if (summaryMessage != null) 2 else 1
            val messagesToSummarize = chatHistory.drop(startIndex)

            val summaryText = summaryMessages(messagesToSummarize)

            //이전 대화 요약을 위한 변수 값 할당 (누적 요약)
            summaryMessage = ChatMessage(
                role = ChatRole.System,
                content = "Previous conversation summary:\n${summaryMessage?.content ?: ""}\nRecent messages summary:\n$summaryText"
            )

            // 히스토리 재구성: 시스템 프롬프트 + 요약 메시지만 유지
            chatHistory.clear()
            chatHistory.add(systemPromptMessage)
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

    // 설정 프롬프트 변경을 실시간 반영하기 위한 구독 관리
    private var settingsScope: CoroutineScope? = null
    private var promptCollectJob: Job? = null

    // Settings 등에서 제공하는 Flow<String> (새 시스템 프롬프트)를 붙여 실시간 반영
    fun attachSystemPromptFlow(promptFlow: Flow<String>, preserveHistory: Boolean = true) {
        // 기존 구독 해제
        promptCollectJob?.cancel()
        if (settingsScope == null) settingsScope = CoroutineScope(Dispatchers.Default)
        promptCollectJob = settingsScope!!.launch {
            promptFlow.collectLatest { newPrompt ->
                updateSystemPrompt(newPrompt, preserveHistory)
            }
        }
    }

    // 구독 해제 (필요 시 화면 파괴 등에서 호출)
    fun detachSystemPromptFlow() {
        promptCollectJob?.cancel()
        promptCollectJob = null
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
                content = "다음 내용을 바탕으로 대화 주제가 무엇인지 간략하게 요약해줘:\n$firstMessage" // 👈
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