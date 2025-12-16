package com.example.logtalk.ui.chat.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.relatedChat.FindRelatedConsultationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.logtalk.core.utils.Logger

data class RelatedConsultationItem(
    val id: String,
    val title: String,
    val date: String,
    val summary: String
)


@Immutable
data class RelatedChatUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val relatedChats: List<RelatedConsultationItem> = emptyList()
)
class RelatedChatViewModel(
    private val consultationId: String, // 현재 상담의 ID (String)
    private val findRelatedConsultationsUseCase: FindRelatedConsultationsUseCase // Use Case 주입
) : ViewModel() {

    // 1. _uiState 필드 정의 (인식이 안 되는 오류 해결)
    private val _uiState = MutableStateFlow(RelatedChatUiState(isLoading = true))
    val uiState: StateFlow<RelatedChatUiState> = _uiState.asStateFlow()

    init {
        loadRelatedConsultations()
    }

    private fun loadRelatedConsultations() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {

            try {
                val currentTitleId = consultationId.toLong()

                val results = findRelatedConsultationsUseCase(currentTitleId, topN = 5)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    relatedChats = results
                )
            } catch (e: Exception) {
                Logger.d("유사상담오류")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "유사 상담을 불러오기 실패)"
                )
            }
        }
    }
    fun onConsultationItemClick(item: RelatedConsultationItem) {
        // TODO: 클릭된 상담 ID로 이동하거나, 해당 상담 내용을 표시하는 로직 구현
        println("Clicked Consultation ID: ${item.id}")
    }
}