package com.example.logtalk.ui.chat.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.relatedChat.FindRelatedConsultationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.logtalk.core.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
@HiltViewModel
class RelatedChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, // 현재 상담의 ID (String)
    private val findRelatedConsultationsUseCase: FindRelatedConsultationsUseCase // Use Case 주입
) : ViewModel() {

    private val consultationId: String = savedStateHandle.get<String>("consultationId")
        ?: throw IllegalArgumentException("아이디가 빠졌어요")
    private val _uiState = MutableStateFlow(RelatedChatUiState(isLoading = true))
    val uiState: StateFlow<RelatedChatUiState> = _uiState.asStateFlow()

    init {
        loadRelatedConsultations()
    }

    private fun loadRelatedConsultations() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            Logger.d("RELATED_CHAT", "1. 진입한 consultationId: $consultationId")

            if (consultationId == "NEW_CHAT_SESSION_FOR_ANALYSIS") {
                Logger.d("RELATED_CHAT", "2. 새 채팅으로 판별되어 빈 목록 반환")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    relatedChats = emptyList()
                )
                return@launch
            }

            try {
                val currentTitleId = consultationId.toLong()

                val results = findRelatedConsultationsUseCase(currentTitleId, topN = 7)

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
    fun onConsultationItemClick(item: RelatedConsultationItem, onSelected: (Long) -> Unit) {
        try {
            val consultationLongId = item.id.toLong()
            onSelected(consultationLongId)
        } catch (e: NumberFormatException) {
            Logger.e("관련채팅불러오기 오류났어요!!@@@@!!@!!@!!")
        }
    }
}