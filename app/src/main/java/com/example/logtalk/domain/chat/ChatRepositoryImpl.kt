package com.example.logtalk.domain.chat

import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.local.TitleData
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.data.Title
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.logtalk.domain.chat.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val titleDao: TitleDao,
    private val llmService: OpenAILLMChatService,
) : ChatRepository {

    // LLM 연결
    override suspend fun getBotResponse(userMessage: String, history: List<Message>): String {
        return llmService.getResponse(userMessage)
    }

    override suspend fun resetHistory() {
        llmService.resetHistory()
    }

    //메세지
    override suspend fun saveMessage(message: Message, parentTitleId: Long): Message {
        val entity = message.toEntity(parentTitleId)

        val newId = messageDao.insertMessage(entity)

        return message.copy(id = newId)
    }

    override fun getMessagesByParentTitleId(parentTitleId: Long): Flow<List<Message>> {
        return messageDao.getMessagesByParentTitleId(parentTitleId).map { messageDataList ->
            messageDataList.map { it.toDomain() }
        }
    }

    //채팅방
    override suspend fun insertTitle(title: Title): Long {
        val entity = title.toEntity()
        return titleDao.insertTitle(entity)
    }

    override fun getAllTitles(): Flow<List<Title>> {
        return titleDao.getAllTitles().map { titleDataList ->
            titleDataList.map { it.toDomain() }
        }
    }

    override suspend fun deleteTitle(title: Title) {
        titleDao.deleteTitle(title.toEntity())
    }

    override suspend fun deleteMessagesByParentTitleId(parentTitleId: Long) {
        messageDao.deleteMessagesByParentTitleId(parentTitleId)
    }

    override suspend fun updateTitleText(titleId: Long, newTitle: String) {
        return titleDao.updateTitleText(titleId, newTitle)
    }
}

//domain -> entity
fun Message.toEntity(parentTitleId: Long): MessageData {
    val senderName = if (this.isUser) "User" else "Bot"
    return MessageData(
        messageId = this.id, // ID가 0이 아니면 업데이트, 0이면 삽입
        parentTitleId = parentTitleId,
        sender = senderName,
        content = this.text,
        createdAt = System.currentTimeMillis() // 저장 시점의 시간을 DB에 기록
    )
}

fun Title.toEntity(): TitleData {
    return TitleData(
        titleId = this.titleId,
        title = this.title,
        embedding = this.embedding,
        createdAt = this.createdAt
    )
}


//entity -> domain

fun MessageData.toDomain(): Message {
    return Message(
        id = this.messageId,
        text = this.content,
        isUser = this.sender == "User",
        // createdAt은 UI/Domain 모델에 없으므로 생략
    )
}

fun TitleData.toDomain(): Title {
    return Title(
        titleId = this.titleId,
        title = this.title,
        embedding = this.embedding,
        createdAt = this.createdAt
    )
}