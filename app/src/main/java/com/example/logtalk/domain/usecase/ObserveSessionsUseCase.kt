package com.example.logtalk.domain.usecase

import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 세션 목록을 실시간으로 관찰하는 UseCase
 * 채팅이 업데이트되면 자동으로 홈 화면도 업데이트됨
 */
class ObserveSessionsUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    operator fun invoke(): Flow<List<Session>> {
        return repository.observeSessions()
    }
}

