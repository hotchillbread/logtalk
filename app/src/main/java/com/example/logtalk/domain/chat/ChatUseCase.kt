package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.ui.chat.data.Message

class ChatUseCase(
    private val repository: ChatRepository
) {
    suspend fun getBotResponseWithMessageUpdate(
        userMessageText: String,
        currentMessages: List<Message>
    ): List<Message> {
        // 1. 사용자 메시지 생성
        val userMessage = Message(
            id = System.currentTimeMillis(),
            text = userMessageText,
            isUser = true
        )
        val messagesWithUser = currentMessages + userMessage

        // 응답 요청
        val botResponseText = repository.getBotResponse(
            userMessage = userMessageText,
            history = currentMessages // 전체 맥락 전달
        )

        // 3. 봇 메시지 생성
        val botMessage = Message(
            id = System.currentTimeMillis() + 1,
            text = botResponseText,
            isUser = false,
        )

        // 4. 새로운 목록 반환
        return messagesWithUser + botMessage
    }

    /**
     * 채팅 기록을 신고합니다.
     */
    suspend fun reportChat() {
        repository.reportChatHistory()
    }

    /**
     * 채팅 기록을 삭제합니다.
     */
    suspend fun deleteChat() {
        repository.deleteChatHistory()
    }
}