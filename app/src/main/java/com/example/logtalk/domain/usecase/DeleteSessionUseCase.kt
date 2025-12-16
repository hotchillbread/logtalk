package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * 세션(채팅방) 삭제 UseCase
 */
class DeleteSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(sessionId: Long) {
        sessionRepository.deleteSession(sessionId)
    }
}

