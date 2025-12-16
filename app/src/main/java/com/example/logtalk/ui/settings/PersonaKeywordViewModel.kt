package com.example.logtalk.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.core.utils.SystemPromptManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonaKeywordViewModel @Inject constructor(
    private val savePersonaUseCase: SavePersonaUseCase,
    private val loadPersonaUseCase: LoadPersonaUseCase,
    private val systemPromptManager: SystemPromptManager,
    private val settingsRepository: com.example.logtalk.data.repositoryImpl.SettingsRepositoryImpl
) : ViewModel() {

    // 사용 가능한 키워드 목록 (확장 가능)
    val availableKeywords: List<PersonaKeyword> = listOf(
        // 톤 카테고리
        PersonaKeyword("톤", "친절한", "따뜻하고 친절한 문체로 응답해주세요"),
        PersonaKeyword("톤", "공식적인", "공식적이고 전문적인 톤을 사용해주세요"),
        PersonaKeyword("톤", "캐주얼한", "편안하고 캐주얼한 말투를 사용해주세요"),

        // 역할 카테고리
        PersonaKeyword("역할", "심리 상담사", "전문 심리 상담사로서 공감하고 경청해주세요"),
        PersonaKeyword("역할", "코치", "사용자의 목표 달성을 돕는 코치 역할을 해주세요"),
        PersonaKeyword("역할", "친구", "가벼운 조언을 주는 친구처럼 응답해주세요"),
        PersonaKeyword("역할", "멘토", "경험 많은 멘토로서 조언과 방향을 제시해주세요"),

        // 응답 스타일
        PersonaKeyword("스타일", "공감적", "사용자의 감정에 깊이 공감하며 응답해주세요"),
        PersonaKeyword("스타일", "분석적", "상황을 논리적으로 분석하고 해결책을 제시해주세요"),
        PersonaKeyword("스타일", "격려적", "긍정적이고 격려하는 태도로 응답해주세요"),

        // 언어
        PersonaKeyword("언어", "한국어", "한국어로 자연스럽게 답변해주세요"),
        PersonaKeyword("언어", "존댓말", "정중한 존댓말을 사용해주세요"),
        PersonaKeyword("언어", "반말", "편안한 반말을 사용해주세요"),

        // 응답 제한
        PersonaKeyword("제약", "간결하게", "핵심만 간단명료하게 전달해주세요"),
        PersonaKeyword("제약", "구체적으로", "구체적인 예시와 함께 설명해주세요"),
        PersonaKeyword("제약", "질문 유도", "사용자가 스스로 생각할 수 있도록 질문을 던져주세요")
    )

    // 선택된 키워드 집합
    private val _selectedKeywords = MutableStateFlow<Set<String>>(emptySet())
    val selectedKeywords: StateFlow<Set<String>> = _selectedKeywords.asStateFlow()

    // 현재 생성된 프롬프트
    private val _currentPrompt = MutableStateFlow("")
    val currentPrompt: StateFlow<String> = _currentPrompt.asStateFlow()

    init {
        loadInitialKeywords()
    }

    /**
     * 초기 키워드 로드
     * 요구사항: 항상 각 카테고리의 첫 번째 키워드 선택된 상태로 시작
     */
    private fun loadInitialKeywords() {
        viewModelScope.launch {
            // 각 카테고리의 첫 번째 키워드 자동 선택
            val firstKeywords = availableKeywords
                .groupBy { it.category }
                .mapNotNull { (_, keywords) -> keywords.firstOrNull()?.name }
                .toSet()

            _selectedKeywords.value = firstKeywords
            generatePrompt()
        }
    }

    /**
     * 키워드 선택/해제 토글
     */
    fun toggleKeyword(keywordName: String) {
        _selectedKeywords.update { current ->
            val mutableSet = current.toMutableSet()
            if (mutableSet.contains(keywordName)) {
                mutableSet.remove(keywordName)
            } else {
                mutableSet.add(keywordName)
            }
            mutableSet
        }
        generatePrompt()
    }

    /**
     * 선택된 키워드들로부터 최종 프롬프트 생성
     */
    private fun generatePrompt() {
        viewModelScope.launch {
            val selected = _selectedKeywords.value
            val fragments = availableKeywords
                .filter { selected.contains(it.name) }
                .map { it.promptFragment }

            val prompt = if (fragments.isEmpty()) {
                "당신은 친절하고 공감 능력이 뛰어난 심리 상담 AI입니다."
            } else {
                // 자연스러운 문장 조합
                "당신은 AI 상담사입니다. " + fragments.joinToString(". ") { it.trimEnd('.', '。') } + "."
            }

            _currentPrompt.value = prompt
        }
    }

    /**
     * 현재 생성된 프롬프트를 저장하고 SystemPromptManager에 반영
     */
    fun savePrompt() {
        viewModelScope.launch {
            val prompt = _currentPrompt.value
            val personaData = PersonaData(description = prompt)

            // PersonaData 저장
            savePersonaUseCase(personaData)

            // 선택된 키워드 저장 (TODO: SettingsRepository에 메서드 구현 필요)
            // settingsRepository.saveSelectedKeywords(_selectedKeywords.value)

            // SystemPromptManager에 실시간 반영
            systemPromptManager.updateSystemPrompt(prompt)
        }
    }

    /**
     * 모든 키워드 선택 해제
     */
    fun clearAllKeywords() {
        _selectedKeywords.value = emptySet()
        generatePrompt()
    }

    /**
     * 카테고리별로 키워드 그룹화
     */
    fun getKeywordsByCategory(): Map<String, List<PersonaKeyword>> {
        return availableKeywords.groupBy { it.category }
    }
}

