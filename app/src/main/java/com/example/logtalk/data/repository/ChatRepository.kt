package com.example.logtalk.data.repository

import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.data.local.ChatDao
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// @Inject constructor 추가
class ChatRepository @Inject constructor(
    private val messageDao: ChatDao,
    private val titleDao: TitleDao, // 필요시 사용
    private val userDao: UserDao,   // 필요시 사용
    private val apiKey: String      // AppModule에서 provideApiKey()로 주입됨
) {
    // ... 기존 구현 유지 ...

    fun getMessages(titleId: Long): Flow<List<MessageData>> {
        return messageDao.getMessagesFlow(titleId) // DAO 메서드 이름 확인 필요
    }

    suspend fun sendMessage(titleId: Long, userText: String): Result<Unit> {
        // ... (이전 코드와 동일) ...
        // OpenAILLMChatService(apiKey = apiKey, ...) 로 사용 가능
        return Result.success(Unit) // 임시 반환
    }
}