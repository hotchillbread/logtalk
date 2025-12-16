package com.example.logtalk.ui.insight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class InsightUiState(
    val totalChats: Int = 0,
    val totalMessages: Int = 0,
    val emotionData: List<EmotionDataPoint> = emptyList(),
    val emotionTrend: String? = null,
    val recentChats: List<ChatSummary> = emptyList(),
    val hasData: Boolean = false  // 데이터 존재 여부
)

data class EmotionDataPoint(
    val date: String,
    val positivePercentage: Float
)

data class ChatSummary(
    val title: String,
    val summary: String,
    val date: String,
    val messageCount: Int
)

class InsightViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(InsightUiState())
    val uiState: StateFlow<InsightUiState> = _uiState.asStateFlow()

    private val database = AppDatabase.getDatabase(application)
    private val titleDao = database.titleDao()
    private val messageDao = database.messageDao()

    init {
        loadInsightData()
    }

    private fun loadInsightData() {
        viewModelScope.launch {
            try {
                // 모든 채팅방 가져오기
                val titles = titleDao.getAllTitles().first()

                if (titles.isEmpty()) {
                    // 데이터가 없으면 빈 상태
                    _uiState.value = InsightUiState(hasData = false)
                    return@launch
                }

                // 총 상담 수
                val totalChats = titles.size

                // 총 메시지 수 계산
                var totalMessages = 0
                val recentChats = mutableListOf<ChatSummary>()

                titles.forEach { title ->
                    val messageCount = messageDao.getMessageCount(title.titleId)
                    totalMessages += messageCount

                    // 최근 3개의 채팅만 요약에 추가
                    if (recentChats.size < 3) {
                        val firstMessage = messageDao.getFirstMessageContent(title.titleId)
                        val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)

                        recentChats.add(
                            ChatSummary(
                                title = title.title.ifEmpty { "새로운 상담" },
                                summary = if (messageCount > 0) {
                                    firstMessage ?: "요약을 생성할 수 없습니다."
                                } else {
                                    "요약을 생성할 수 없습니다."
                                },
                                date = dateFormat.format(Date(title.createdAt)),
                                messageCount = messageCount
                            )
                        )
                    }
                }

                // 감정 데이터는 추후 GPT 분석으로 구현
                // 현재는 더미 데이터 사용
                val emotionData = generateDummyEmotionData()
                val trend = calculateTrend(emotionData)

                _uiState.value = InsightUiState(
                    totalChats = totalChats,
                    totalMessages = totalMessages,
                    emotionData = emotionData,
                    emotionTrend = trend,
                    recentChats = recentChats,
                    hasData = true
                )
            } catch (e: Exception) {
                // 에러 발생 시 빈 상태
                _uiState.value = InsightUiState(hasData = false)
            }
        }
    }

    private fun generateDummyEmotionData(): List<EmotionDataPoint> {
        // 최근 7일간의 감정 데이터 생성 (임시)
        val dateFormat = SimpleDateFormat("M/d", Locale.KOREAN)
        val calendar = Calendar.getInstance()
        val data = mutableListOf<EmotionDataPoint>()

        var currentPercentage = 55f + (Math.random() * 5).toFloat()

        for (i in 6 downTo 0) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_MONTH, -i)

            val change = (Math.random() * 10 - 5).toFloat()
            currentPercentage = (currentPercentage + change).coerceIn(40f, 80f)

            data.add(
                EmotionDataPoint(
                    date = dateFormat.format(calendar.time),
                    positivePercentage = currentPercentage
                )
            )
        }

        return data
    }

    private fun calculateTrend(data: List<EmotionDataPoint>): String? {
        if (data.size < 2) return null

        val recent = data.takeLast(3)
        val previous = data.dropLast(3).takeLast(3)

        if (previous.isEmpty()) return null

        val recentAvg = recent.map { it.positivePercentage }.average()
        val previousAvg = previous.map { it.positivePercentage }.average()

        val diff = recentAvg - previousAvg

        return when {
            diff > 5 -> "최근 긍정 감정 +${String.format("%.0f", diff)}% 상승!"
            diff < -5 -> "최근 긍정 감정 ${String.format("%.0f", diff)}% 하락"
            else -> "최근 긍정 감정 유지 중"
        }
    }

    fun refreshData() {
        loadInsightData()
    }
}

