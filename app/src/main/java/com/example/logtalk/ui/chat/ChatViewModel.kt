package com.example.logtalk.ui.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.ui.chat.data.ChatUiState
import com.example.logtalk.ui.chat.data.Message
import kotlinx.coroutines.delay // 임시 딜레이를 위해 사용
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    //UI 상태
    var uiState by mutableStateOf(ChatUiState())
        private set

    // 입력 텍스트 변경
    fun updateTextInput(newText: String) {
        uiState = uiState.copy(textInput = newText)
    }

    // 메시지 전송 로직
    fun sendMessage() {
        if (uiState.textInput.isBlank()) return

        val userMessageText = uiState.textInput
        updateTextInput("") // 입력 필드 초기화

        //사용자 메시지를 목록에 추가
        val userMessage = Message(
            id = System.currentTimeMillis(), // 임시값
            text = userMessageText,
            isUser = true
        )
        val newMessages = uiState.messages + userMessage
        uiState = uiState.copy(
            messages = newMessages,
            isLoading = true
        )

        /*봇 응답, 목록에 추가
        viewModelScope.launch {
            // TODO: 실제로는 서버 API 호출 또는 로컬 LLM 추론 로직 구현

            val botResponse = generateBotResponse(userMessageText)
            val botMessage = Message(
                id = System.currentTimeMillis() + 1, // 임시 ID
                text = botResponse,
                isUser = false,
            )

            uiState = uiState.copy(
                messages = uiState.messages + botMessage,
                isLoading = false
            )
            // TODO: 메시지 스크롤을 가장 아래로 이동시키는 로직 구현 (Compose LazyListState 사용)
        } */
    }

    // TODO: 음성 메시지 전송 로직 구현 (후순위)
    fun sendVoiceMessage() {
        // 음성 녹음 시작/중지 및 변환 로직 구현
    }

    // TODO: 비슷한 상담 찾기 로직 구현
    fun findSimilarConsultation() {
        // 홈 화면 또는 별도의 검색 화면으로 이동/API 호출 로직 구현
    }

    // TODO: 채팅 신고 로직 구현 (후순위)
    fun reportChat() {
        // 신고 호출 로직 구현
    }

    // TODO: 채팅 삭제 로직 구현
    fun deleteChat() {
        // 채팅 기록 삭제 API 호출 및 UI 업데이트 로직 구현
    }
    // TODO: 뒤로 가기
}