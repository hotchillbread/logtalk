package com.example.logtalk.core.utils.model

import java.io.File

interface SttService {
    suspend fun transcribeAudio(audioFile: File): String
}

// 2. 구현체
class OpenAISttService(private val apiKey: String) : SttService {
    // ... 클라이언트 초기화 로직 (LLM과 동일한 클라이언트 사용 가능)
    private val openAIClient: OpenAI = OpenAI(token = apiKey)

    override suspend fun transcribeAudio(audioFile: File): String {
        // 실제 Whisper API 호출 로직
        val request = AudioTranscribeRequest(
            audioFile = audioFile,
            model = ModelId("whisper-1")
        )
        val transcription = openAIClient.audio.transcriptions.create(request)
        return transcription.text
    }
}