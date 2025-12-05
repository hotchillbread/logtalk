package com.example.logtalk.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle // Navigation Argument 받기용
) : ViewModel() {

    // "titleId"는 NavHost에서 route 설정할 때 넘겨주는 키값과 같아야 함
    private val currentTitleId: Long = savedStateHandle.get<Long>("titleId") ?: 0L

    // UI 상태 관리 (초기값 설정)
    private val _uiState = MutableStateFlow(ChatUiState(titleId = currentTitleId))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // ViewModel이 생성되자마자 DB 관찰 시작
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            // Repository에서 Flow<List<MessageData>>를 가져옴
            chatRepository.getMessages(currentTitleId)
                .map { entityList ->
                    // ★ 핵심: DB Entity(MessageData) -> UI Model(Message) 변환
                    entityList.map { entity -> entity.toUiModel() }
                }
                .collect { uiMessages ->
                    // 변환된 리스트를 State에 업데이트 -> UI가 자동으로 리컴포지션됨
                    _uiState.update { currentState ->
                        currentState.copy(messages = uiMessages)
                    }
                }
        }
    }

    // 메시지 전송
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Repository 호출 (DB 저장 및 OpenAI 통신 수행)
            // UI는 DB에 저장된 내용을 Flow로 감지하므로, 여기서 리스트를 수동으로 add할 필요 없음
            val result = chatRepository.sendMessage(currentTitleId, text)

            result.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    // --- Mapper 함수 (Entity -> UI Model) ---
    private fun MessageData.toUiModel(): Message {
        // DB의 'sender' 필드를 보고 내 메시지인지 판단
        val isMyMessage = (this.sender == "user")

        return Message(
            text = this.content,
            isUser = isMyMessage,
            // 아래 필드들은 DB에 아직 컬럼이 없으므로 일단 null 처리
            // 추후 MessageData 엔티티에 relatedConsultation 등의 컬럼을 추가하면 여기서 매핑 가능
            relatedConsultation = null,
            relatedDate = null,
            directQuestion = null
        )
    }
}