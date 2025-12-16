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


//Groomy 화면 ViewModel
//채팅 횟수 기반으로 감정 progress를 계산
@HiltViewModel
class GroomyViewModel @Inject constructor(
    getTotalMessageCount: GetTotalMessageCountUseCase
) : ViewModel() {
//GetTotalMessageCountUseCase → GroomyViewModel
    companion object {
        private const val TAG = "GroomyViewModel"
    }
    //ViewModel이 언제 만들어졌는지 확인하는 디버깅 코드
    init {
        Log.d(TAG, "GroomyViewModel initialized")
    }

    //전체 메시지 수
    val totalMessageCount: StateFlow<Int> = getTotalMessageCount() //Flow<Int>반환
        .onEach { count -> //흘러가는 값 확인용
            Log.d(TAG, "Total message count: $count")
        }
        .stateIn( //Flow -> StateFlow로 변환
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    //progress 퍼센트(0~100)
    val progressPercentage: StateFlow<Int> = totalMessageCount
        .map { count -> count.coerceAtMost(100) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    //progress 레벨 5단계
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

    //progress 레벨에 따른 감정 메시지
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

