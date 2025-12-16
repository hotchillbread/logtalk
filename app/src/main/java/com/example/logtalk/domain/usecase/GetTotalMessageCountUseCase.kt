package com.example.logtalk.domain.usecase

import com.example.logtalk.data.local.MessageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 전체 채팅 메시지 횟수를 가져오는 UseCase
 * Groomy 화면의 프로그레스 바 계산에 사용
 */
class GetTotalMessageCountUseCase @Inject constructor(
    private val messageDao: MessageDao
) {
    /**
     * 모든 채팅방의 총 메시지 개수를 실시간으로 관찰
     * @return Flow<Int> 총 메시지 개수
     */
    operator fun invoke(): Flow<Int> {
        return messageDao.getTotalMessageCount()
    }
}

