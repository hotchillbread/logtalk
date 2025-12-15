package com.example.logtalk.ui.home

// 세션 데이터 클래스
data class SessionData(
    val id: Long,
    val title: String,
    val lastMessage: String,
    val timeAgo: String
)
