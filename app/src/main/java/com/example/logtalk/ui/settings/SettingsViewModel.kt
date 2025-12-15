package com.example.logtalk.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.core.utils.SystemPromptManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//  Domain 레이어의 UseCase는 프로젝트에 맞게 정의하고 Hilt 모듈에 바인딩해야 함
// 임시 인터페이스 정의 (실제 코드는 Domain Layer에 있어야 함)
interface SavePersonaUseCase { suspend operator fun invoke(persona: PersonaData) }
interface LoadPersonaUseCase { suspend operator fun invoke(): PersonaData }
// 인수로 Unit을 받도록 변경
interface DeleteAllRecordsUseCase { suspend fun executeDelete() }

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePersonaUseCase: SavePersonaUseCase,
    private val loadPersonaUseCase: LoadPersonaUseCase,
    private val deleteAllRecordsUseCase: DeleteAllRecordsUseCase,
    private val systemPromptManager: SystemPromptManager,
    private val settingsRepository: com.example.logtalk.data.repositoryImpl.SettingsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState


    init {
        loadInitialData()
        observeSystemPromptChanges()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // 초기 Persona Data 로드 시도
            val loadedPersona = try { loadPersonaUseCase() } catch (_: Exception) { PersonaData() }
            _uiState.update {
                it.copy(persona = loadedPersona, currentEditingPersona = loadedPersona)
            }
            // SystemPromptManager에 저장된 프롬프트 반영
            systemPromptManager.updateSystemPrompt(loadedPersona.description)
        }
    }

    /**
     * SystemPromptManager의 변경사항을 구독하여 실시간 반영
     */
    private fun observeSystemPromptChanges() {
        viewModelScope.launch {
            systemPromptManager.systemPromptFlow.collect { newPrompt ->
                // 프롬프트가 변경되면 DB에서 다시 로드
                if (newPrompt.isNotBlank() && newPrompt != _uiState.value.persona.description) {
                    val updatedPersona = try {
                        loadPersonaUseCase()
                    } catch (_: Exception) {
                        PersonaData(description = newPrompt)
                    }
                    _uiState.update {
                        it.copy(
                            persona = updatedPersona,
                            currentEditingPersona = updatedPersona
                        )
                    }
                }
            }
        }
    }

    /**
     * 데이터 새로고침 (키워드 선택 화면에서 돌아올 때 호출)
     */
    fun refreshData() {
        loadInitialData()
    }

    fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    private suspend fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            // 편집 모드 진입 및 취소
            SettingsIntent.ClickEditPersona -> {
                _uiState.update {
                    it.copy(isEditingPersona = true, currentEditingPersona = it.persona) // 원본을 임시 상태로 복사
                }
            }
            SettingsIntent.CancelEdit -> {
                _uiState.update {
                    it.copy(isEditingPersona = false, currentEditingPersona = it.persona) // 원본으로 복구
                }
            }
            // 텍스트 입력 시 임시 상태 업데이트
            is SettingsIntent.UpdateEditingDescription -> {
                _uiState.update {
                    it.copy(currentEditingPersona = it.currentEditingPersona.copy(description = intent.newDescription))
                }
            }
            // 데이터 저장
            is SettingsIntent.SavePersona -> {
                savePersonaUseCase(intent.persona)

                // 편집 모드에서 저장한 경우 키워드 정보 삭제 (키워드 기반이 아님)
                settingsRepository.saveSelectedKeywords(emptySet())

                // SystemPromptManager에 새 프롬프트 반영 (실시간 업데이트)
                systemPromptManager.updateSystemPrompt(intent.persona.description)
                _uiState.update {
                    it.copy(persona = intent.persona, isEditingPersona = false)
                }
            }

            // 4단계: 삭제 관련 로직 추가

            SettingsIntent.ShowDeleteDialog -> {
                _uiState.update { it.copy(showDeleteDialog = true) }
            }
            SettingsIntent.DismissDialog -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
            }
            SettingsIntent.ConfirmDelete -> {
                // UseCase 호출하여 모든 기록 삭제 실행
                deleteAllRecordsUseCase.executeDelete() // !! UseCase가 실제로 실행됨 !!
                _uiState.update {
                    // 다이얼로그를 닫고, 필요하다면 초기 상태로 복구 로직 추가
                    it.copy(showDeleteDialog = false)
                }
            }
        }
    }
}