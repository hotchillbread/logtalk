package com.example.logtalk.domain.chat

import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.ui.chat.data.Message
import kotlinx.coroutines.delay

class ChatRepositoryImpl(
    private val chatDao: MessageDao,
    private val llmService: OpenAILLMChatService,
) : ChatRepository {

    override suspend fun saveMessage(message: Message, parentTitleId: Long) {
        val entity = message.toEntity(parentTitleId)
        chatDao.insertMessage(entity)
    }

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

//message 를 db에 넣을 messageData로 변환해주는 코드
fun Message.toEntity(parentTitleId: Long): MessageData {

    val senderName = if (this.isUser) "User" else "Bot"

    return MessageData(
        parentTitleId = parentTitleId,
        sender = senderName,
        content = this.text,
    )
}