package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * UseCase: 최근 세션 목록 가져오기
 */
class GetRecentSessionsUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(): List<Session> {
        return repository.getRecentSessions()
    }
}

