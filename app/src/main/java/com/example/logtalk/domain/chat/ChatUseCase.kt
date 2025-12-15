package com.example.logtalk.domain.chat

import android.util.Log
import com.example.logtalk.ui.chat.data.Message

class ChatUseCase(
    private val repository: ChatRepository
) {
    suspend fun getBotResponseWithMessageUpdate(
        userMessageText: String,
        currentMessages: List<Message>,
        parentTitleId: Long = 1L // 임시 기본값 (나중에 HomeViewModel에서 전달받아야 함)
    ): List<Message> {
        Log.d("ChatUseCase", "getBotResponseWithMessageUpdate 시작")
        Log.d("ChatUseCase", "사용자 메시지: $userMessageText")

        // 1. 사용자 메시지 생성
        val userMessage = Message(
            id = System.currentTimeMillis(),
            text = userMessageText,
            isUser = true
        )

        Log.d("ChatUseCase", "사용자 메시지 생성 완료: $userMessage")

        // 2. 사용자 메시지를 DB에 저장
        try {
            Log.d("ChatUseCase", "사용자 메시지 DB 저장 시작")
            repository.saveMessage(userMessage, parentTitleId)
            Log.d("ChatUseCase", "사용자 메시지 DB 저장 완료")
        } catch (e: Exception) {
            Log.e("ChatUseCase", "사용자 메시지 DB 저장 실패", e)
            throw e
        }

        val messagesWithUser = currentMessages + userMessage
        Log.d("ChatUseCase", "현재 메시지 수: ${messagesWithUser.size}")

        // 3. 봇 응답 요청
        Log.d("ChatUseCase", "봇 응답 요청 시작")
        val botResponseText = try {
            repository.getBotResponse(
                userMessage = userMessageText,
                history = currentMessages // 전체 맥락 전달
            )
        } catch (e: Exception) {
            Log.e("ChatUseCase", "봇 응답 요청 실패", e)
            throw e
        }
        Log.d("ChatUseCase", "봇 응답 수신: $botResponseText")

        // 4. 봇 메시지 생성
        val botMessage = Message(
            id = System.currentTimeMillis() + 1,
            text = botResponseText,
            isUser = false,
        )

        Log.d("ChatUseCase", "봇 메시지 생성 완료: $botMessage")

        // 5. 봇 메시지를 DB에 저장
        try {
            Log.d("ChatUseCase", "봇 메시지 DB 저장 시작")
            repository.saveMessage(botMessage, parentTitleId)
            Log.d("ChatUseCase", "봇 메시지 DB 저장 완료")
        } catch (e: Exception) {
            Log.e("ChatUseCase", "봇 메시지 DB 저장 실패", e)
            throw e
        }

        // 6. 새로운 목록 반환
        val finalMessages = messagesWithUser + botMessage
        Log.d("ChatUseCase", "최종 메시지 수: ${finalMessages.size}")
        return finalMessages
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