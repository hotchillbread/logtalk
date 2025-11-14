package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * UseCase: 새 세션 생성
 */
class CreateSessionUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(): Long {
        return repository.createSession()
    }
}

