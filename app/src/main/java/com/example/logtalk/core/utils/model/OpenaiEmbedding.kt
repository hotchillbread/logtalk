package com.example.logtalk.core.utils.model

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.api.embedding.Embedding
import com.example.logtalk.core.utils.Logger

interface EmbeddingService {
    suspend fun createEmbeddings(texts: List<String>): List<List<Double>>
}

class OpenAIEmbeddingService(private val apiKey: String) : EmbeddingService {

    private val openAIClient: OpenAI = OpenAI(
        token = apiKey
    )

    override suspend fun createEmbeddings(texts: List<String>): List<List<Double>> {
        // 임베딩 요청 객체 생성
        val request = EmbeddingRequest(
            model = ModelId("text-embedding-3-small"),
            input = texts
        )

        try {
            val response = openAIClient.embeddings(request)

            return response.embeddings.map { embedding: Embedding ->
                embedding.embedding.map { it.toDouble() }
            }
        } catch (e: Exception) {
            Logger.e("Embedding API 호출 중 오류")
            return emptyList()
        }
    }
}