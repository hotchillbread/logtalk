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

data class Title(
    val titleId: Long,
    val title: String,
    val embedding: ByteArray?, // 1024차원 벡터 저장 (TypeConverter 필요)
    val createdAt: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Title

        if (titleId != other.titleId) return false
        if (createdAt != other.createdAt) return false
        if (title != other.title) return false
        if (!embedding.contentEquals(other.embedding)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titleId.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val textInput: String = "",
    val isLoading: Boolean = false, // 메시지 전송 중/응답 대기 중 로딩 상태
    val errorMessage: String? = null // 에러 메시지
)