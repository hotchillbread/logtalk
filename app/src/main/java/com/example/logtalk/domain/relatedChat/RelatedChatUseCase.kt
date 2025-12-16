package com.example.logtalk.domain.relatedChat

import com.example.logtalk.core.utils.model.EmbeddingService
import com.example.logtalk.core.utils.calculateCosineSimilarity
import com.example.logtalk.domain.relatedChat.RelatedChatRepository
import com.example.logtalk.ui.chat.viewmodel.RelatedConsultationItem
import java.nio.ByteBuffer
import java.nio.DoubleBuffer
import javax.inject.Inject

interface FindRelatedConsultationsUseCase {
    suspend operator fun invoke(currentTitleId: Long, topN: Int = 5): List<RelatedConsultationItem>
}

class FindRelatedConsultationsUseCaseImpl @Inject constructor(
    private val repository: RelatedChatRepository,
    private val embeddingService: EmbeddingService
) : FindRelatedConsultationsUseCase {

    // 임베딩 ByteArray를 List<Double>로 변환하는 헬퍼 함수
    private fun convertByteArrayToDoubleList(byteArray: ByteArray?): List<Double>? {
        if (byteArray == null) return null
        val doubleBuffer = ByteBuffer.wrap(byteArray).asDoubleBuffer()
        val doubleArray = DoubleArray(doubleBuffer.limit())
        doubleBuffer.get(doubleArray)
        return doubleArray.toList()
    }

    override suspend operator fun invoke(currentTitleId: Long, topN: Int): List<RelatedConsultationItem> {

        val currentTitle = repository.getCurrentConsultationTitle(currentTitleId) ?: return emptyList()

        val currentEmbeddingList = embeddingService.createEmbeddings(listOf(currentTitle)).firstOrNull() ?: return emptyList()

        val allTitles = repository.getAllEmbeddingsForAnalysis(currentTitleId)

        val similarityResults = allTitles.mapNotNull { titleData ->
            val otherEmbeddingList = convertByteArrayToDoubleList(titleData.embedding)

            if (otherEmbeddingList != null) {
                val similarity = calculateCosineSimilarity(currentEmbeddingList, otherEmbeddingList)

                val summary = repository.getFirstMessageContent(titleData.titleId) ?: "요약 없음"

                Triple(similarity, titleData, summary)
            } else {
                null
            }
        }

        return similarityResults
            .sortedByDescending { it.first }
            .take(topN)
            .map { (similarity, titleData, summary) ->
                RelatedConsultationItem(
                    id = titleData.titleId.toString(),
                    title = titleData.title,
                    date = java.text.SimpleDateFormat("yyyy.MM.dd").format(titleData.createdAt),
                    summary = summary
                )
            }
    }
}