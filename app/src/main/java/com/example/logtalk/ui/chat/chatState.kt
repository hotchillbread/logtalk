package com.example.logtalk.ui.chat

// 화면에 보여질 상태 데이터의 집합
data class ChatUiState(
    // 1. 현재 채팅방 ID (어떤 방인지 식별)
    val titleId: Long = 0L,

    // 2. 화면에 뿌려줄 메시지 리스트 (UI용 모델인 Message 사용)
    val messages: List<Message> = emptyList(),

    // 3. 로딩 상태 (AI 답변 기다리는 중인지)
    val isLoading: Boolean = false,

    // 4. 에러 메시지 (네트워크 오류 등 발생 시)
    val errorMessage: String? = null
)