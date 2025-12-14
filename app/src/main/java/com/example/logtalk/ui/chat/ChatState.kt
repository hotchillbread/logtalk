package com.example.logtalk.ui.chat.data

data class Message(
    val id: Long, // 고유 ID 추가
    val text: String,
    val isUser: Boolean, // true면 사용자 메시지, false면 봇 메시지
    // 하단에 표시할 '관련 상담 내용' 필드
    val relatedConsultation: String? = null,
    // 관련 상담이 있을 경우의 날짜
    val relatedDate: String? = null,
    // 관련 상담 아래에 표시할 다이렉트 질문
    val directQuestion: String? = null
)

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val textInput: String = "",
    val isLoading: Boolean = false, // 메시지 전송 중/응답 대기 중 로딩 상태
    val error: String? = null // 에러 메시지
)