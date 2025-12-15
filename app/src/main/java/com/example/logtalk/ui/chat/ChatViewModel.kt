package com.example.logtalk.ui.chat.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.chat.ChatUseCase
import com.example.logtalk.ui.chat.data.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    //UI 상태
    var uiState by mutableStateOf(ChatUiState())
        private set

    // 입력 텍스트 변경
    fun updateTextInput(newText: String) {
        uiState = uiState.copy(textInput = newText)
    }

    // 메시지 전송 로직
    fun sendMessage() {
        Log.d("ChatViewModel", "sendMessage 호출됨")
        if (uiState.textInput.isBlank()) {
            Log.d("ChatViewModel", "입력이 비어있음")
            return
        }

        val userMessageText = uiState.textInput
        Log.d("ChatViewModel", "메시지 전송: $userMessageText")
        updateTextInput("") // 입력 필드 초기화

        // 로딩 상태 표시
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            try {
                Log.d("ChatViewModel", "ChatUseCase 호출 시작")
                // ChatUseCase를 통해 사용자 메시지 전송 및 봇 응답 받기
                val updatedMessages = chatUseCase.getBotResponseWithMessageUpdate(
                    userMessageText = userMessageText,
                    currentMessages = uiState.messages
                )

                Log.d("ChatViewModel", "업데이트된 메시지 수: ${updatedMessages.size}")
                uiState = uiState.copy(
                    messages = updatedMessages,
                    isLoading = false
                )
                Log.d("ChatViewModel", "UI 상태 업데이트 완료")
            } catch (e: Exception) {
                // 에러 발생 시 처리
                Log.e("ChatViewModel", "메시지 전송 중 에러 발생", e)
                uiState = uiState.copy(
                    isLoading = false
                )
            }
        }
    }

    // TODO: 음성 메시지 전송 로직 구현 (후순위)
    fun sendVoiceMessage() {
        // 음성 녹음 시작/중지 및 변환 로직 구현
    }

    // TODO: 비슷한 상담 찾기 로직 구현
    fun findSimilarConsultation() {
        // 홈 화면 또는 별도의 검색 화면으로 이동/API 호출 로직 구현
    }

    // 채팅 신고 로직
    fun reportChat() {
        viewModelScope.launch {
            try {
                chatUseCase.reportChat()
                // TODO: 신고 완료 메시지 표시
            } catch (e: Exception) {
                // TODO: 에러 처리
            }
        }
    }

    // 채팅 삭제 로직
    fun deleteChat() {
        viewModelScope.launch {
            try {
                chatUseCase.deleteChat()
                // 채팅 기록 초기화
                uiState = uiState.copy(messages = emptyList())
                // TODO: 삭제 완료 메시지 표시
            } catch (e: Exception) {
                // TODO: 에러 처리
            }
        }
    }
}

