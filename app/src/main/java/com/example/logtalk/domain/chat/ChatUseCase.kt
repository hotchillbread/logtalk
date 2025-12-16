package com.example.logtalk.domain.chat

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.core.utils.model.OpenIllegitimateSummarize
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.data.Title
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//메시지 전송 & 응답
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userMessageText: String,
        history: List<Message>,
        parentTitleId: Long
    ): Message {
        val userMessage = Message(id = 0L, text = userMessageText, isUser = true)
        val savedUserMessage = chatRepository.saveMessage(userMessage, parentTitleId)

        // 응답 요청
        val updatedHistory = history + listOf(savedUserMessage)

        val botResponseText = chatRepository.getBotResponse(userMessageText, updatedHistory)

        val botMessage = Message(id = 0L, text = botResponseText, isUser = false)
        val savedBotMessage = chatRepository.saveMessage(botMessage, parentTitleId)

        return savedBotMessage
    }
}

// 새 채팅방 생성
class CreateNewChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Long {
        val newTitle = Title(
            titleId = 0L, // Room에서 자동 생성될 것입니다.
            title = "새 대화", // 임시 제목
            embedding = null,
            createdAt = System.currentTimeMillis()
        )

        return chatRepository.insertTitle(newTitle)
    }
}

// 채팅방 목록 조회
class GetChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Title>> {
        return chatRepository.getAllTitles()
    }
}

//채팅 기록 조회
class GetChatHistoryUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(parentTitleId: Long): Flow<List<Message>> {
        return chatRepository.getMessagesByParentTitleId(parentTitleId)
    }
}

//채팅방 삭제
class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(title: Title) {

        chatRepository.deleteTitle(title)

//llm 히스토리 초기화(메세지 배열 비우기)
        chatRepository.resetHistory()
    }
}

class GenerateAndSaveTitleUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val titleSummarizer: OpenIllegitimateSummarize // 요약 전용 LLM
) {

    suspend operator fun invoke(titleId: Long, firstMessageText: String) {

        // LLM에게 제목 요약 요청
        val newTitleText = titleSummarizer.getResponse(firstMessageText)

        // DB 업데이트
        chatRepository.updateTitleText(titleId, newTitleText)


        Logger.d("$titleId: 제목 업데이트=> '$newTitleText'")
    }
}