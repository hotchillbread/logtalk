package com.example.logtalk.ui.settings

/**
 * 페르소나 키워드 데이터 클래스
 * @param category 키워드가 속한 카테고리 (예: "톤", "역할", "언어" 등)
 * @param name 키워드 이름 (UI에 표시될 이름)
 * @param promptFragment 이 키워드가 선택될 때 프롬프트에 추가될 문장 조각
 */
data class PersonaKeyword(
    val category: String,
    val name: String,
    val promptFragment: String
)

