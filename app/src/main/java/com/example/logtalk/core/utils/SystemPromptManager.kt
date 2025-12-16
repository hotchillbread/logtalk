package com.example.logtalk.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 앱 전체에서 사용하는 시스템 프롬프트를 관리하는 싱글톤 클래스
 * Settings에서 프롬프트를 변경하면 ChatRepository가 실시간으로 반영
 */
@Singleton
class SystemPromptManager @Inject constructor() {

    private val _systemPrompt = MutableStateFlow(
        "당신은 친절하고 공감 능력이 뛰어난 심리 상담 AI입니다. 사용자의 이야기를 경청하고, 따뜻한 조언을 제공하며, 항상 긍정적이고 지지적인 태도를 유지합니다."
    )

    val systemPromptFlow: StateFlow<String> = _systemPrompt.asStateFlow()

    fun updateSystemPrompt(newPrompt: String) {
        _systemPrompt.value = newPrompt
    }

    fun getCurrentPrompt(): String = _systemPrompt.value
}

