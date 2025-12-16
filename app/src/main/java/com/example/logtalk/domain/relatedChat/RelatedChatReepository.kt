package com.example.logtalk.domain.relatedChat

import com.example.logtalk.data.local.TitleData
import com.example.logtalk.ui.chat.viewmodel.RelatedConsultationItem

interface RelatedChatRepository {

    suspend fun getCurrentConsultationTitle(currentTitleId: Long): String?

    suspend fun getAllEmbeddingsForAnalysis(currentTitleId: Long): List<TitleData>

    suspend fun getFirstMessageContent(titleId: Long): String?

}