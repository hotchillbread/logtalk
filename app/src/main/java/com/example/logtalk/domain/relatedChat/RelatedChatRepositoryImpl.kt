package com.example.logtalk.domain.relatedChat

import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.TitleData
import com.example.logtalk.domain.relatedChat.RelatedChatRepository
import javax.inject.Inject
import com.example.logtalk.core.utils.Logger

class RelatedChatRepositoryImpl @Inject constructor(
    private val titleDao: TitleDao,
    private val messageDao: MessageDao
) : RelatedChatRepository {

    override suspend fun getCurrentConsultationTitle(currentTitleId: Long): String? {
        return messageDao.getFirstMessageContent(currentTitleId)
    }

    override suspend fun getAllEmbeddingsForAnalysis(currentTitleId: Long): List<TitleData> {
        val r = titleDao.getAllEmbeddingsExceptCurrent(currentTitleId)
        Logger.d(r.toString())
        return r
    }

    override suspend fun getFirstMessageContent(titleId: Long): String? {
        return messageDao.getFirstMessageContent(titleId)
    }
}