package com.example.logtalk.domain.chat

import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.core.utils.model.OpenAIEmbeddingService
import com.example.logtalk.core.utils.model.OpenIllegitimateSummarize
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.data.Title
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer
import javax.inject.Inject

//메시지 전송 & 응답
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userMessageText: String,
        history: List<Message>,
        parentTitleId: Long
    ): Message {
        val userMessage = Message(id = 0L, text = userMessageText, isUser = true)
        val savedUserMessage = chatRepository.saveMessage(userMessage, parentTitleId)

        // 응답 요청
        val updatedHistory = history + listOf(savedUserMessage)

        val botResponseText = chatRepository.getBotResponse(userMessageText, updatedHistory)

        val botMessage = Message(id = 0L, text = botResponseText, isUser = false)
        val savedBotMessage = chatRepository.saveMessage(botMessage, parentTitleId)

        return savedBotMessage
    }
}

// 새 채팅방 생성
class CreateNewChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Long {
        val newTitle = Title(
            titleId = 0L, // Room에서 자동 생성될 것입니다.
            title = "새 대화", // 임시 제목
            embedding = null,
            createdAt = System.currentTimeMillis()
        )

        return chatRepository.insertTitle(newTitle)
    }
}

// 채팅방 목록 조회
class GetChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Title>> {
        return chatRepository.getAllTitles()
    }
}

//채팅 기록 조회
class GetChatHistoryUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(parentTitleId: Long): Flow<List<Message>> {
        return chatRepository.getMessagesByParentTitleId(parentTitleId)
    }
}

//채팅방 삭제
class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(title: Title) {

        chatRepository.deleteTitle(title)

//llm 히스토리 초기화(메세지 배열 비우기)
        chatRepository.resetHistory()
    }
}

class GenerateAndSaveTitleUseCase @Inject constructor(
    private val titleDao: TitleDao,
    private val titleSummarizer: OpenIllegitimateSummarize,
    private val embeddingGenerator: OpenAIEmbeddingService
) {

    suspend operator fun invoke(titleId: Long, firstMessageText: String) {
        // 1. LLM에게 제목 요약 요청
        val newTitleText = titleSummarizer.getResponse(firstMessageText)

        // 2. 임베딩 생성 (List<List<Double>> 형태)
        val embeddingList: List<List<Double>> = embeddingGenerator.createEmbeddings(listOf(newTitleText))

        // 단일 제목에 대한 단일 임베딩 벡터 (List<Double>)를 가져옵니다.
        val doubleEmbedding: List<Double>? = embeddingList.firstOrNull()
        Logger.d(doubleEmbedding.toString())
        if (doubleEmbedding == null) {
            Logger.e("Embedding 생성 실패: $newTitleText 에 대한 임베딩 벡터를 얻지 못했습니다.")
            return
        }

        // 3. 임베딩 벡터를 TitleData의 ByteArray 타입에 맞게 변환
        // 변환 함수들이 널이 아닌 ByteArray를 반환한다고 가정
        val embeddingByteArray: ByteArray = doubleEmbedding.toDoubleArray().toByteArray()

        // 4. 현재 TitleData 가져오기
        val currentTitle = titleDao.getTitleById(titleId)

        if (currentTitle != null) {
            // 5. 제목과 임베딩 업데이트
            val updatedTitle = currentTitle.copy( // TitleData의 data class copy 사용
                title = newTitleText,
                embedding = embeddingByteArray
            )

            titleDao.updateTitle(updatedTitle)
            Logger.d("$titleId: 제목 및 임베딩 업데이트 완료 => '$newTitleText'")
        } else {
            Logger.e("Title with id $titleId not found. 업데이트를 건너뜁니다.")
        }
    }
}

fun DoubleArray.toByteArray(): ByteArray {
    val byteBuffer = ByteBuffer.allocate(this.size * 8) // Double은 8바이트
    byteBuffer.asDoubleBuffer().put(this)
    return byteBuffer.array()
}