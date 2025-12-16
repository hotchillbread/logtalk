package com.example.logtalk.data.repository

import android.annotation.SuppressLint
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.TitleData
import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionRepository 실제 구현체 (Room Database 연동)
 *
 * Note: java.time API는 desugaring을 통해 API 24+에서도 사용 가능합니다.
 * build.gradle.kts에서 coreLibraryDesugaring이 활성화되어 있습니다.
 */
@SuppressLint("NewApi")
@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val titleDao: TitleDao,
    private val messageDao: MessageDao
) : SessionRepository {

    /**
     * TitleData를 Session 도메인 모델로 변환
     */
    private suspend fun TitleData.toSession(): Session {
        val messages = messageDao.getMessagesByParentTitleId(this.titleId).first()
        val lastMessage = messages.lastOrNull()?.content

        return Session(
            id = this.titleId,
            title = this.title.ifEmpty { "새 대화" },
            lastMessage = lastMessage,
            createdAt = Instant.ofEpochMilli(this.createdAt),
            updatedAt = messages.lastOrNull()?.let {
                Instant.ofEpochMilli(it.createdAt)
            } ?: Instant.ofEpochMilli(this.createdAt)
        )
    }

    override suspend fun getRecentSessions(): List<Session> {
        val titles = titleDao.getAllTitles().first()
        return titles.map { it.toSession() }
            .sortedByDescending { it.updatedAt }
    }

    override suspend fun searchSessions(query: String): List<Session> {
        val titles = titleDao.getAllTitles().first()
        return titles
            .map { it.toSession() }
            .filter { session ->
                // TitleData의 title 필드를 기준으로 검색
                session.title.contains(query, ignoreCase = true) ||
                        // 마지막 메시지 내용도 검색
                        (session.lastMessage?.contains(query, ignoreCase = true) == true)
            }
            .sortedByDescending { it.updatedAt }
    }

    override suspend fun getSessionById(id: Long): Session? {
        val titles = titleDao.getAllTitles().first()
        val titleData = titles.find { it.titleId == id } ?: return null
        return titleData.toSession()
    }

    override suspend fun createSession(): Long {
        val newTitle = TitleData(
            title = "새 대화",
            embedding = null,
            createdAt = System.currentTimeMillis()
        )
        return titleDao.insertTitle(newTitle)
    }

    override suspend fun deleteSession(id: Long) {
        val titles = titleDao.getAllTitles().first()
        val titleData = titles.find { it.titleId == id } ?: return
        titleDao.deleteTitle(titleData)
    }

    override fun observeSessions(): Flow<List<Session>> {
        return titleDao.getAllTitles().map { titles ->
            titles.map { it.toSession() }
                .sortedByDescending { it.updatedAt }
        }
    }
}

