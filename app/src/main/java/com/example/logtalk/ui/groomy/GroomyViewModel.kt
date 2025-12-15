package com.example.logtalk.ui.groomy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.usecase.GetTotalMessageCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Groomy 화면의 ViewModel
 * 채팅 횟수 기반으로 감정 상태(프로그레스)를 계산
 */
@HiltViewModel
class GroomyViewModel @Inject constructor(
    getTotalMessageCount: GetTotalMessageCountUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "GroomyViewModel"
    }

    init {
        Log.d(TAG, "GroomyViewModel initialized")
    }

    /**
     * 전체 채팅 횟수
     */
    val totalMessageCount: StateFlow<Int> = getTotalMessageCount()
        .onEach { count ->
            Log.d(TAG, "Total message count: $count")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * 채팅 횟수 기반 프로그레스 퍼센트 (0~100)
     * 1회당 1% 증가
     */
    val progressPercentage: StateFlow<Int> = totalMessageCount
        .map { count -> count.coerceAtMost(100) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * 프로그레스 레벨 (1~5)
     * 0~24%: 1번
     * 25~49%: 2번
     * 50~74%: 3번
     * 75~99%: 4번
     * 100%: 5번
     */
    val progressLevel: StateFlow<Int> = progressPercentage
        .map { percentage ->
            when {
                percentage in 0..24 -> 1
                percentage in 25..49 -> 2
                percentage in 50..74 -> 3
                percentage in 75..99 -> 4
                else -> 5 // 100%
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1
        )

    /**
     * 감정 상태 메시지
     */
    val emotionMessage: StateFlow<String> = progressLevel
        .map { level ->
            when (level) {
                1 -> "많이 힘드시네요..."
                2 -> "조금 힘든 상태예요"
                3 -> "보통이에요"
                4 -> "기분이 좋네요!"
                5 -> "최고의 기분이에요!"
                else -> ""
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "많이 힘드시네요..."
        )
}

