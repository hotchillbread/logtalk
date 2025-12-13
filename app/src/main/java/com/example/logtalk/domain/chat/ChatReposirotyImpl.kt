package com.example.logtalk.domain.chat

import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.core.utils.model.OpenaiLLM
import kotlinx.coroutines.delay

class ChatRepositoryImpl(
    private val llmService: OpenAILLMChatService,
    // TitleSummarizationLLMService도 주입받을 수 있음 (제목 관련 로직이 필요하다면)
) : ChatRepository {

    override suspend fun getBotResponse(userMessage: String, history: List<Message>): String {
        //불러오기
        return llmService.getResponse(userMessage)
    }

    override suspend fun reportChatHistory() {
        // 그냥 신고하는 척만?
        delay(1000L)
    }

    override suspend fun deleteChatHistory() {
        // 삭제 로직
        delay(1000L)
        llmService.resetHistory()
    }
}
