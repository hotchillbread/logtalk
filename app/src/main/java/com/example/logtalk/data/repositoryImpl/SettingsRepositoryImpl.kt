package com.example.logtalk.data.repositoryImpl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.logtalk.ui.settings.DeleteAllRecordsUseCase
import com.example.logtalk.ui.settings.LoadPersonaUseCase
import com.example.logtalk.ui.settings.PersonaData
import com.example.logtalk.ui.settings.SavePersonaUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SavePersonaUseCase, LoadPersonaUseCase, DeleteAllRecordsUseCase {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("persona_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PERSONA_DESCRIPTION = "persona_description"
        private const val KEY_PERSONA_STYLE = "persona_style"
        private const val KEY_SELECTED_KEYWORDS = "selected_keywords" // 선택된 키워드 목록
    }

    override suspend fun invoke(persona: PersonaData) {
        // SharedPreferences에 PersonaData 저장
        prefs.edit {
            putString(KEY_PERSONA_DESCRIPTION, persona.description)
            putString(KEY_PERSONA_STYLE, persona.style.name)
        }
    }

    /**
     * 선택된 키워드 저장 (PersonaKeywordViewModel에서 호출용)
     */
    fun saveSelectedKeywords(keywords: Set<String>) {
        prefs.edit {
            putStringSet(KEY_SELECTED_KEYWORDS, keywords)
        }
    }

    /**
     * 선택된 키워드 로드
     */
    fun loadSelectedKeywords(): Set<String> {
        return prefs.getStringSet(KEY_SELECTED_KEYWORDS, null) ?: emptySet()
    }

    override suspend fun executeDelete() {
        // TODO: 모든 메시지 삭제 - MessageDao에 deleteAll 메서드가 있다면 사용
        // messageDao.deleteAll()
    }

    override suspend fun invoke(): PersonaData {
        // SharedPreferences에서 PersonaData 로드
        val description = prefs.getString(KEY_PERSONA_DESCRIPTION, null)
            ?: "당신은 친절하고 공감 능력이 뛰어난 심리 상담 AI입니다. 사용자의 이야기를 경청하고, 따뜻한 조언을 제공하며, 항상 긍정적이고 지지적인 태도를 유지합니다."

        val styleStr = prefs.getString(KEY_PERSONA_STYLE, null)
        val style = try {
            com.example.logtalk.ui.settings.PersonaStyle.valueOf(styleStr ?: "SUPPORTIVE")
        } catch (_: Exception) {
            com.example.logtalk.ui.settings.PersonaStyle.SUPPORTIVE
        }

        return PersonaData(description = description, style = style)
    }
}