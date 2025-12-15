package com.example.logtalk.ui.relatedChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.relatedChat.GetSimilarSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelatedChatViewModel @Inject constructor(
    private val getSimilarSessionsUseCase: GetSimilarSessionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RelatedChatState())
    val uiState: StateFlow<RelatedChatState> = _uiState

    fun fetchRelatedChats(sessionId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val similarSessions = getSimilarSessionsUseCase(sessionId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                relatedChats = similarSessions.map {
                    RelatedChat(
                        date = "", // You need to format the date
                        title = it.title,
                        summary = it.lastMessage ?: ""
                    )
                }
            )
        }
    }
}
