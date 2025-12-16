package com.example.logtalk.domain.chat

import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.data.Title
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    // LLM
    suspend fun getBotResponse(userMessage: String, history: List<Message>): String
    suspend fun resetHistory() // LLM 초기화

    // 메시지
    suspend fun saveMessage(message: Message, parentTitleId: Long): Message
    fun getMessagesByParentTitleId(parentTitleId: Long): Flow<List<Message>>

    // 채팅방
    suspend fun insertTitle(title: Title): Long // 새로 생성된 titleId 반환
    fun getAllTitles(): Flow<List<Title>>
    suspend fun deleteTitle(title: Title)
    suspend fun deleteMessagesByParentTitleId(parentTitleId: Long)

    suspend fun updateTitleText(titleId: Long, newTitle: String)

}