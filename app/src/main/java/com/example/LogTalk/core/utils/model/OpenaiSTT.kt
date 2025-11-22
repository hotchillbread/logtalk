package com.example.logtalk.core.utils.model

import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.io.RawSource
import java.io.File
import okio.source

interface SttService {
    suspend fun transcribeAudio(audioFile: File): String
}

class OpenAISttService(private val apiKey: String) : SttService {

    private val client: OpenAI = OpenAI(token = apiKey)

    override suspend fun transcribeAudio(audioFile: File): String {


        val fileSource = FileSource(
            name = audioFile.name,
            source = audioFile.source() as RawSource
        )

        val request = TranscriptionRequest(
            audio = fileSource,           // ✔ FileSource 사용
            model = ModelId("gpt-4o-mini-transcribe")
        )

        val result = client.transcription(request)
        return result.text
    }
}
