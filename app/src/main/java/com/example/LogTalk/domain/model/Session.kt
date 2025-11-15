package com.example.logtalk.domain.model

import java.time.Instant

/**
 * Domain 모델: 세션 (채팅 대화 단위)
 */
data class Session(
    val id: Long,
    val title: String,
    val lastMessage: String?,
    val createdAt: Instant,
    val updatedAt: Instant?
)

