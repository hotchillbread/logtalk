package com.example.logtalk.ui.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.chat.CreateNewChatUseCase
import com.example.logtalk.domain.chat.DeleteChatUseCase
import com.example.logtalk.domain.chat.GenerateAndSaveTitleUseCase
import com.example.logtalk.domain.chat.GetChatHistoryUseCase
import com.example.logtalk.domain.chat.SendMessageUseCase
import com.example.logtalk.ui.chat.data.ChatUiState
import com.example.logtalk.ui.chat.data.Title
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.domain.chat.ChatRepository


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val createNewChatUseCase: CreateNewChatUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val generateAndSaveTitleUseCase: GenerateAndSaveTitleUseCase,

    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    //네비게이션 세팅해줘야함
    private val initialTitleId: Long = savedStateHandle.get<Long>("titleId") ?: -1L

    var uiState by mutableStateOf(ChatUiState())
        private set

    private var currentTitleId: Long = initialTitleId

    private var isFirstMessageSent: Boolean = false
    private var currentChatTitle: Title? = null

    //라우팅 핸들링용
    init {
        handleChatInitialization()
    }

    //초기화
    private fun handleChatInitialization() {
        viewModelScope.launch {
            if (currentTitleId == -1L) {
                currentTitleId = createNewChatUseCase()
                isFirstMessageSent = false
            } else {
                isFirstMessageSent = true
            }

            observeChatHistory(currentTitleId)
        }
    }

    private fun observeChatHistory(titleId: Long) {
        viewModelScope.launch {
            getChatHistoryUseCase(titleId).collectLatest { messages ->

                Logger.d("Flow COLLECTED: Message count = ${messages.size}")

                uiState = uiState.copy(
                    messages = messages,
                    isLoading = false
                )
                isFirstMessageSent = messages.size >= 2
            }
        }
    }

    fun updateTextInput(newText: String) {
        uiState = uiState.copy(textInput = newText)
    }

    fun sendMessage() {
        if (uiState.textInput.isBlank() || uiState.isLoading) return
        val userMessageText = uiState.textInput
        val history = uiState.messages

        val newUserMessage = Message(
            id = System.currentTimeMillis(), // 임시 ID
            text = userMessageText,
            isUser = true
        )

        //메세지 배열에 추가라 여기는 문제없음
        uiState = uiState.copy(
            messages = history + newUserMessage,
            textInput = "",
            isLoading = true,
            errorMessage = null // 이전 오류 메시지 초기화
        )

        viewModelScope.launch {
            try {
                sendMessageUseCase(userMessageText, history, currentTitleId)

                if (!isFirstMessageSent) {
                    Logger.d("제목 텍스트: $userMessageText")
                    generateAndSaveTitleUseCase(currentTitleId, userMessageText)
                    isFirstMessageSent = true
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    errorMessage = "메시지 전송 실패: ${e.localizedMessage}",
                    messages = history,
                    isLoading = false
                )
                Logger.e("전송 실패")
            }
        }
    }

    fun reportChat() {
        viewModelScope.launch {
            uiState = uiState.copy(errorMessage = "신고가 접수되었습니다. (로직 임시 처리)")
        }
    }

    fun deleteChat(onChatDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                val titleToDelete = Title(titleId = currentTitleId, title = "", embedding = null, createdAt = 0L)
                deleteChatUseCase(titleToDelete)
                onChatDeleted()
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = "채팅방 삭제 실패: ${e.localizedMessage}")
            }
        }
    }

    fun findSimilarConsultation() {
        uiState = uiState.copy(errorMessage = "유사 상담 찾기 화면으로 이동합니다.")
    }

    fun sendVoiceMessage() {
        uiState = uiState.copy(errorMessage = "음성 메시지 기능은 준비 중입니다.")
    }
}