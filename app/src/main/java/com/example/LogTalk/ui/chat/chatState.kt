// 📍 위치: com.example.logtalk.ui.chat.ChatState.kt

package com.example.logtalk.ui.chat

import java.util.UUID // Message 구분을 위한 ID (예시)

/**
 * ChatScreen UI의 모든 상태를 나타내는 데이터 클래스.
 */
data class ChatUiState(
    // 채팅 목록
    val messages: List<Message> = emptyList(),

    // 사용자가 입력 중인 텍스트
    val inputText: String = "",

    // AI가 응답을 생성 중인지 여부
    val isLoading: Boolean = false,

    // 음성 녹음 중인지 여부
    val isRecording: Boolean = false
)

/**
 * 개별 채팅 메시지를 나타내는 데이터 클래스.
 */
data class Message(
    val id: String = UUID.randomUUID().toString(), // LazyColumn에서 키로 사용
    val text: String,
    val isFromUser: Boolean // true: 사용자 메시지, false: AI 메시지
)