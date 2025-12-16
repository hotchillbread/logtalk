package com.example.logtalk.domain.relatedChat

import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.TitleData
import com.example.logtalk.domain.relatedChat.RelatedChatRepository

class RelatedChatRepositoryImpl(
    private val titleDao: TitleDao,
    private val messageDao: MessageDao
) : RelatedChatRepository {

    override suspend fun getCurrentConsultationTitle(currentTitleId: Long): String? {
        return messageDao.getFirstMessageContent(currentTitleId)
    }

    override suspend fun getAllEmbeddingsForAnalysis(currentTitleId: Long): List<TitleData> {
        return titleDao.getAllEmbeddingsExceptCurrent(currentTitleId)
    }

    override suspend fun getFirstMessageContent(titleId: Long): String? {
        return messageDao.getFirstMessageContent(titleId)
    }
}