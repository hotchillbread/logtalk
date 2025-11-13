package com.example.logtalk.data.repository

import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionRepository 임시 구현체 (Mock 데이터)
 * TODO: Room Database와 연결하여 실제 구현 필요
 */
@Singleton
class SessionRepositoryImpl @Inject constructor(
    // TODO: SessionDao 주입 필요
) : SessionRepository {

    // 임시 메모리 저장소
    private val sessions = mutableListOf<Session>()
    private var nextId = 1L

    override suspend fun getRecentSessions(): List<Session> {
        // 임시: 샘플 데이터 생성
        if (sessions.isEmpty()) {
            sessions.addAll(
                listOf(
                    Session(
                        id = 1,
                        title = "세션 1",
                        lastMessage = "안녕하세요! 첫 번째 메시지입니다.",
                        createdAt = Instant.now(),
                        updatedAt = Instant.now()
                    ),
                    Session(
                        id = 2,
                        title = "세션 2",
                        lastMessage = "두 번째 세션의 마지막 메시지",
                        createdAt = Instant.now(),
                        updatedAt = Instant.now()
                    )
                )
            )
            nextId = 3
        }
        return sessions.sortedByDescending { it.updatedAt }
    }

    override suspend fun searchSessions(query: String): List<Session> {
        return sessions.filter {
            it.title.contains(query, ignoreCase = true) ||
                    (it.lastMessage?.contains(query, ignoreCase = true) == true)
        }.sortedByDescending { it.updatedAt }
    }

    override suspend fun getSessionById(id: Long): Session? {
        return sessions.find { it.id == id }
    }

    override suspend fun createSession(): Long {
        val now = Instant.now()
        val newSession = Session(
            id = nextId++,
            title = "새 세션 ${nextId - 1}",
            lastMessage = null,
            createdAt = now,
            updatedAt = now
        )
        sessions.add(newSession)
        return newSession.id
    }

    override suspend fun deleteSession(id: Long) {
        sessions.removeAll { it.id == id }
    }

    override fun observeSessions(): Flow<List<Session>> {
        return flowOf(sessions.sortedByDescending { it.updatedAt })
    }
}

