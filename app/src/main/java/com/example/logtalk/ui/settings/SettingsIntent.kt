package com.example.logtalk.ui.settings

sealed interface SettingsIntent {
    // 페르소나 편집 관련
    data object ClickEditPersona : SettingsIntent
    data object CancelEdit : SettingsIntent
    data class SavePersona(val persona: PersonaData) : SettingsIntent

    // 편집 중 실시간 업데이트
    data class UpdateEditingDescription(val newDescription: String) : SettingsIntent

    // 데이터 삭제 관련
    data object ShowDeleteDialog : SettingsIntent
    data object ConfirmDelete : SettingsIntent
    data object DismissDialog : SettingsIntent
}