package com.example.logtalk.domain.chat

//import com.example.logtalk.data.local.MessageData
import com.example.logtalk.ui.chat.data.Message

interface ChatRepository {
    suspend fun getBotResponse(userMessage: String, history: List<Message>): String

    suspend fun saveMessage(message: Message, parentTitleId: Long)

    suspend fun reportChatHistory()

    suspend fun deleteChatHistory()
}