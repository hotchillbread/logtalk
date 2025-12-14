package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * UseCase: 세션 검색
 */
class SearchSessionsUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(query: String): List<Session> {
        return repository.searchSessions(query)
    }
}

