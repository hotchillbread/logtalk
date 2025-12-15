package com.example.logtalk.ui.settings

// 페르소나 스타일 Enum 정의 (나중에 확장 가능)
enum class PersonaStyle {
    SUPPORTIVE, // 지지적
    ANALYTICAL, // 분석적
    EMPATHIC    // 공감적
}

data class PersonaData(
    // 제목 없이 내용(description)만 사용
    val description: String = "당신은 친절하고 공감 능력이 뛰어난 심리 상담 AI입니다. 사용자의 이야기를 경청하고, 따뜻한 조언을 제공하며, 항상 긍정적이고 지지적인 태도를 유지합니다.",
    val style: PersonaStyle = PersonaStyle.SUPPORTIVE
)

data class AppInfoData(
    val version: String,
    val developer: String,
    val contact: String
)