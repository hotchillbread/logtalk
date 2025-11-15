package com.example.logtalk.data.repositoryImpl

import com.example.logtalk.data.local.UserDao
import com.example.logtalk.data.local.MessageDao

import com.example.logtalk.ui.settings.DeleteAllRecordsUseCase
import com.example.logtalk.ui.settings.LoadPersonaUseCase
import com.example.logtalk.ui.settings.PersonaData
import com.example.logtalk.ui.settings.SavePersonaUseCase
import javax.inject.Inject

// 1. UseCase 인터페이스 상속 및 구현
// 2. @Inject constructor를 붙여 Hilt가 이 구현체를 생성할 수 있게 함
class SettingsRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao
) : SavePersonaUseCase, LoadPersonaUseCase, DeleteAllRecordsUseCase {

    override suspend fun invoke(persona: PersonaData) {
        // [TODO: RoomDB에 PersonaData 저장 로직 구현]
    }

    override suspend fun invoke(): PersonaData {
        // [TODO: RoomDB에서 PersonaData 로드 로직 구현]
        return PersonaData() // 임시로 기본값 반환
    }

    override suspend fun invoke(unit: Unit) {
        // [TODO: 모든 기록 삭제 로직 구현]
    }
}