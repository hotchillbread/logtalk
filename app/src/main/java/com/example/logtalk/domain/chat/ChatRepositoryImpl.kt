package com.example.logtalk.domain.chat

import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.core.utils.SystemPromptManager
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.local.Title as TitleEntity
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.ui.chat.data.Message
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val titleDao: TitleDao,
    private val llmService: OpenAILLMChatService,
    private val systemPromptManager: SystemPromptManager
) : ChatRepository {


    // LLM 연결
    override suspend fun getBotResponse(userMessage: String, history: List<Message>): String {
        return llmService.getResponse(userMessage)
    }

    override suspend fun reportChatHistory() {
        // 채팅 히스토리 신고 로직
        delay(1000L)
    }

    override suspend fun deleteChatHistory() {
        // 채팅 히스토리 삭제 로직
        delay(1000L)
        llmService.resetHistory()
    }

    fun resetHistory() {
        llmService.resetHistory()
    }

    //메세지
    override suspend fun saveMessage(message: Message, parentTitleId: Long) {
        val entity = message.toEntity(parentTitleId)
        messageDao.insertMessage(entity)
    }
}

//domain -> entity
fun Message.toEntity(parentTitleId: Long): MessageData {
    val senderName = if (this.isUser) "User" else "Bot"
    return MessageData(
        messageId = this.id,
        parentTitleId = parentTitleId,
        sender = senderName,
        content = this.text,
        createdAt = System.currentTimeMillis()
    )
}

//entity -> domain
fun MessageData.toDomain(): Message {
    return Message(
        id = this.messageId,
        text = this.content,
        isUser = this.sender == "User"
    )
}