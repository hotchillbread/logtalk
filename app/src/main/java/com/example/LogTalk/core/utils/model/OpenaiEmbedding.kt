package com.example.logtalk.core.utils.model

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.api.embedding.Embedding

// 1. Embedding 관련 기능을 정의하는 인터페이스
interface EmbeddingService {
    /**
     * 입력된 텍스트 리스트에 대한 임베딩 벡터를 생성합니다.
     * @param texts 임베딩을 생성할 텍스트 리스트
     * @return 임베딩된 벡터(Float 리스트)의 리스트
     */
    suspend fun createEmbeddings(texts: List<String>): List<List<Double>>
}

// 2. OpenAI Embedding API 구현체
// DI를 통해 API Key를 주입받습니다.
class OpenAIEmbeddingService(private val apiKey: String) : EmbeddingService {

    // OpenAI SDK 클라이언트 초기화
    private val openAIClient: OpenAI = OpenAI(
        token = apiKey
    )

    /**
     * OpenAI의 text-embedding-3-small 모델을 사용하여 임베딩을 생성합니다.
     */
    override suspend fun createEmbeddings(texts: List<String>): List<List<Double>> {
        // 임베딩 요청 객체 생성
        val request = EmbeddingRequest(
            model = ModelId("text-embedding-3-small"), // 추천 임베딩 모델
            input = texts // 임베딩을 요청할 텍스트 리스트
        )

        try {
            // API 호출 및 결과 수신
            val response = openAIClient.embeddings(request)

            // 응답에서 벡터 데이터만 추출하여 Double 리스트 형태로 반환
            return response.embeddings.map { embedding: Embedding ->
                embedding.embedding.map { it.toDouble() }
            }
        } catch (e: Exception) {
            // 오류 로깅 및 빈 리스트 또는 예외를 던지는 방식으로 오류 처리
            println("Embedding API 호출 중 오류 발생: ${e.message}")
            // 실제 앱에서는 Logger.e 등을 사용하여 로깅해야 합니다.
            return emptyList()
        }
    }
}