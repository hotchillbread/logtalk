package com.example.logtalk.ui.chat.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ----------------------------------------------------
// 1. 데이터 모델 정의
// ----------------------------------------------------

/**
 * 유사 상담 목록의 각 항목을 나타내는 데이터 클래스.
 * UI 이미지에 맞춰 '제목', '날짜', '요약' 정보를 담습니다.
 */
data class RelatedConsultationItem(
    val id: String,
    val title: String, // 예: "너 생각에는 내가 재수강을 하는게 좋을까?"
    val date: String, // 예: "2025.11.02"
    val summary: String // 예: "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"
)

/**
 * 유사 상담 화면의 UI 상태를 정의합니다.
 */
@Immutable
data class RelatedChatUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val relatedChats: List<RelatedConsultationItem> = emptyList()
)

// ----------------------------------------------------
// 2. ViewModel 정의
// ----------------------------------------------------

class RelatedChatViewModel(
    // TODO: Repository 또는 UseCase를 주입하여 실제 데이터를 가져오도록 수정
    private val consultationId: String // 현재 상담의 ID
) : ViewModel() {

    private val _uiState = MutableStateFlow(RelatedChatUiState(isLoading = true))
    val uiState: StateFlow<RelatedChatUiState> = _uiState.asStateFlow()

    init {
        loadRelatedConsultations()
    }

    /**
     * 현재 상담과 관련된 유사 상담 목록을 불러옵니다.
     */
    private fun loadRelatedConsultations() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                // TODO: 실제 API 호출 또는 DB 접근 로직 구현
                // 예시 더미 데이터
                val mockData = listOf(
                    RelatedConsultationItem("1", "너 생각에는 내가 재수강을 하는게 좋을까?", "2025.11.09", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                    RelatedConsultationItem("2", "너 생각에는 내가 재수강을 하는게 좋을까?", "2025.11.02", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                    RelatedConsultationItem("3", "너 생각에는 내가 재수강을 하는게 좋을까?", "2025.11.02", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                    RelatedConsultationItem("4", "너 생각에는 내가 재수강을 하는게 좋을까?", "2025.11.02", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    relatedChats = mockData
                )
            } catch (e: Exception) {
                // 에러 처리
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "유사 상담을 불러오는 데 실패했습니다."
                )
                e.printStackTrace()
            }
        }
    }

    /**
     * 유사 상담 항목을 클릭했을 때의 동작을 처리합니다.
     * @param item 클릭된 상담 항목
     */
    fun onConsultationItemClick(item: RelatedConsultationItem) {
        // TODO: 클릭된 상담 ID로 이동하거나, 해당 상담 내용을 표시하는 로직 구현
        println("Clicked Consultation ID: ${item.id}")
    }

    // TODO: 필요하다면 새로고침 로직 (refresh) 추가
}