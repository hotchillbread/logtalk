// 📍 위치: com.example.logtalk.ui.chat.ChatViewModel.kt

package com.example.logtalk.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // 1. 백킹 필드 (Backing Field): ViewModel 내부에서만 수정 가능
    private val _uiState = MutableStateFlow(ChatUiState())

    // 2. 공개 상태 (Public State): UI(View)에서는 이것을 구독 (읽기 전용)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // --- UI 이벤트 핸들러 ---

    /**
     * 입력창 텍스트가 변경될 때 호출됩니다.
     */
    fun onInputTextChanged(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(inputText = newText)
        }
    }

    /**
     * '전송' 버튼을 클릭할 때 호출됩니다.
     */
    fun sendMessage() {
        val messageText = _uiState.value.inputText
        if (messageText.isBlank()) return

        // 1. 사용자 메시지를 즉시 UI에 추가
        val userMessage = Message(text = messageText, isFromUser = true)
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage, // 리스트에 추가
                inputText = "" // 입력창 비우기
            )
        }

        // 2. AI 응답을 비동기로 요청 (Coroutine 사용)
        viewModelScope.launch {
            // 로딩 상태 시작
            _uiState.update { it.copy(isLoading = true) }

            // (시뮬레이션) AI가 1.5초간 생각함
            // TODO: 여기에 실제 AI API나 UseCase를 호출하는 로직을 넣으세요.
            delay(1500)

            val aiResponse = Message(text = "'$messageText'라고 하셨군요!", isFromUser = false)

            // 3. AI 응답을 UI에 추가하고 로딩 상태 종료
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + aiResponse,
                    isLoading = false
                )
            }
        }
    }

    /**
     * '음성 녹음' 버튼을 클릭할 때 호출됩니다.
     */
    fun onVoiceRecordClick() {
        val isCurrentlyRecording = _uiState.value.isRecording

        // TODO: 실제 음성 녹음 시작/중지 로직 구현

        _uiState.update {
            it.copy(isRecording = !isCurrentlyRecording)
        }
    }

    /**
     * '뒤로 가기' 버튼 클릭 시
     */
    fun onBackClick() {
        // TODO: Navigation 로직 (예: navController.popBackStack())
    }

    /**
     * '케밥 메뉴' 버튼 클릭 시
     */
    fun onKebabMenuClick() {
        // TODO: 드롭다운 메뉴 표시 로직
    }
}
