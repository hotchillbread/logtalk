package com.example.logtalk.ui.insight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.TitleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class InsightUiState(
    val totalChats: Int = 0,
    val totalMessages: Int = 0,
    val emotionData: List<EmotionDataPoint> = emptyList(),
    val emotionTrend: String? = null,
    val recentChats: List<ChatSummary> = emptyList(),
    val hasData: Boolean = false
)

data class EmotionDataPoint(
    val date: String,
    val positivePercentage: Float
)

data class ChatSummary(
    val sessionId: Long,
    val title: String,
    val summary: String,
    val date: String,
    val messageCount: Int
)

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val titleDao: TitleDao,
    private val messageDao: MessageDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(InsightUiState())
    val uiState: StateFlow<InsightUiState> = _uiState.asStateFlow()

    init {
        loadInsightData()
    }

    private fun loadInsightData() {
        viewModelScope.launch {
            try {
                // 모든 채팅방 가져오기
                val titles: List<TitleData> = titleDao.getAllTitles().first()

                if (titles.isEmpty()) {
                    _uiState.value = InsightUiState(hasData = false)
                    return@launch
                }

                // 총 상담 수
                val totalChats = titles.size

                // 총 메시지 수 (사용자 메시지만)
                val totalMessages = messageDao.getTotalUserMessageCount()

                val recentChats = mutableListOf<ChatSummary>()
                val emotionDataPoints = mutableListOf<EmotionDataPoint>()

                val dateFormat = SimpleDateFormat("M/d", Locale.KOREAN)
                val fullDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)

                titles.forEach { titleData ->
                    // 사용자 메시지 개수만 카운트
                    val userMessageCount = messageDao.getUserMessageCount(titleData.titleId)

                    // 최근 3개의 채팅만 요약에 추가
                    if (recentChats.size < 3) {
                        val firstMessage = messageDao.getFirstMessageContent(titleData.titleId)

                        recentChats.add(
                            ChatSummary(
                                sessionId = titleData.titleId,
                                title = titleData.title.ifEmpty { "새로운 상담" },
                                summary = if (userMessageCount > 0) {
                                    firstMessage ?: "요약을 생성할 수 없습니다."
                                } else {
                                    "요약을 생성할 수 없습니다."
                                },
                                date = fullDateFormat.format(Date(titleData.createdAt)),
                                messageCount = userMessageCount
                            )
                        )
                    }

                    // 감정 데이터: 저장된 값이 있으면 사용, 없으면 AI 분석 (여기서는 임시 더미)
                    if (titleData.emotionPercentage != null) {
                        // 이미 분석된 감정 데이터 사용 (과거 데이터 변경 안 함)
                        emotionDataPoints.add(
                            EmotionDataPoint(
                                date = dateFormat.format(Date(titleData.createdAt)),
                                positivePercentage = titleData.emotionPercentage
                            )
                        )
                    } else {
                        // TODO: OpenAI API로 감정 분석 후 DB에 저장
                        // 임시 더미 데이터
                        val dummyPercentage = 50f + (Math.random() * 30).toFloat()
                        emotionDataPoints.add(
                            EmotionDataPoint(
                                date = dateFormat.format(Date(titleData.createdAt)),
                                positivePercentage = dummyPercentage
                            )
                        )

                        // 실제로는 여기서 AI 분석 후 titleData.copy(emotionPercentage = ...)로 업데이트
                    }
                }

                // 최근 7일 데이터만 그래프에 표시
                val recentEmotionData = emotionDataPoints.takeLast(7)
                val trend = calculateTrend(recentEmotionData)

                _uiState.value = InsightUiState(
                    totalChats = totalChats,
                    totalMessages = totalMessages,
                    emotionData = recentEmotionData,
                    emotionTrend = trend,
                    recentChats = recentChats,
                    hasData = true
                )
            } catch (e: Exception) {
                android.util.Log.e("InsightViewModel", "Error loading insight data", e)
                _uiState.value = InsightUiState(hasData = false)
            }
        }
    }

    private fun calculateTrend(data: List<EmotionDataPoint>): String? {
        if (data.size < 2) return null

        val recent = data.takeLast(3)
        val previous = data.dropLast(3).takeLast(3)

        if (previous.isEmpty()) return null

        val recentAvg = recent.map { it.positivePercentage }.average()
        val previousAvg = previous.map { it.positivePercentage }.average()

        val diff = recentAvg - previousAvg

        // 긍정 감정 기준: 높을수록 좋음
        return when {
            diff > 5 -> "최근 긍정 감정 +${String.format(Locale.getDefault(), "%.0f", diff)}% 상승!"
            diff < -5 -> "최근 긍정 감정 ${String.format(Locale.getDefault(), "%.0f", diff)}% 하락"
            else -> "최근 감정 상태 유지 중"
        }
    }

    fun refreshData() {
        loadInsightData()
    }
}

