# AI 페르소나 키워드 설정 기능

## 개요
키워드 선택 기반으로 AI의 성격과 응답 스타일을 자동으로 설정하는 기능입니다.

## 구현된 파일

### 1. PersonaKeyword.kt
- 키워드 데이터 모델
- `category`: 키워드 카테고리 (톤, 역할, 스타일, 언어, 제약)
- `name`: 키워드 이름
- `promptFragment`: 프롬프트 문장 조각

### 2. PersonaKeywordViewModel.kt
- 키워드 선택 상태 관리
- 프롬프트 자동 생성
- SystemPromptManager와 연동하여 실시간 반영
- **주요 기능:**
  - `toggleKeyword()`: 키워드 선택/해제
  - `generatePrompt()`: 선택된 키워드로 프롬프트 자동 생성
  - `savePrompt()`: 생성된 프롬프트를 저장하고 실시간 반영
  - `clearAllKeywords()`: 모든 키워드 선택 해제

### 3. KeywordSelectionScreen.kt
- 키워드 선택 UI
- 카테고리별 키워드 그룹화
- FilterChip을 사용한 키워드 선택
- 실시간 프롬프트 미리보기
- 저장 및 적용 버튼

## 사용 방법

### Navigation 설정
기존 SettingsScreen에서 KeywordSelectionScreen으로 이동할 수 있도록 네비게이션을 추가하세요:

```kotlin
// SettingsScreen.kt에 버튼 추가
Button(onClick = { navController.navigate("keyword_selection") }) {
    Text("키워드로 설정하기")
}

// Navigation Graph에 경로 추가
composable("keyword_selection") {
    KeywordSelectionScreen(
        onNavigateBack = { navController.popBackStack() }
    )
}
```

### 프롬프트 실시간 반영 흐름

1. **키워드 선택**
   - 사용자가 키워드 Chip을 클릭
   - `PersonaKeywordViewModel.toggleKeyword()` 호출
   - 선택된 키워드 Set 업데이트

2. **프롬프트 자동 생성**
   - `generatePrompt()` 자동 호출
   - 선택된 키워드의 `promptFragment`들을 조합
   - `currentPrompt` StateFlow 업데이트
   - UI에 실시간 반영

3. **저장 및 적용**
   - 사용자가 "저장 및 적용" 버튼 클릭
   - `savePrompt()` 호출
   - `PersonaData` 저장 (UseCase)
   - `SystemPromptManager.updateSystemPrompt()` 호출
   - **기존 채팅 세션에도 즉시 반영**

### SystemPromptManager 연동

`SystemPromptManager`는 싱글톤으로 앱 전체에서 시스템 프롬프트를 관리합니다:

```kotlin
@Singleton
class SystemPromptManager @Inject constructor() {
    private val _systemPrompt = MutableStateFlow("...")
    val systemPromptFlow: StateFlow<String> = _systemPrompt.asStateFlow()
    
    fun updateSystemPrompt(newPrompt: String) {
        _systemPrompt.value = newPrompt // ← 여기서 업데이트
    }
}
```

`ChatRepositoryImpl`에서 이 Flow를 구독하고 있으므로:
- 설정에서 프롬프트 변경 시
- **진행 중인 채팅 세션도 즉시 새 프롬프트 적용**
- LLM 히스토리의 첫 번째 시스템 메시지만 교체 (대화 내용 유지)

## 키워드 목록

### 톤
- 친절한
- 공식적인
- 캐주얼한

### 역할
- 심리 상담사
- 코치
- 친구
- 멘토

### 스타일
- 공감적
- 분석적
- 격려적

### 언어
- 한국어
- 존댓말
- 반말

### 제약
- 간결하게
- 구체적으로
- 질문 유도

## 예시 프롬프트

**선택:** 친절한 + 심리 상담사 + 공감적 + 한국어 + 존댓말

**생성된 프롬프트:**
```
당신은 AI 상담사입니다. 따뜻하고 친절한 문체로 응답해주세요. 전문 심리 상담사로서 공감하고 경청해주세요. 사용자의 감정에 깊이 공감하며 응답해주세요. 한국어로 자연스럽게 답변해주세요. 정중한 존댓말을 사용해주세요.
```

## 확장 방법

새로운 키워드를 추가하려면 `PersonaKeywordViewModel.kt`의 `availableKeywords` 리스트에 추가하세요:

```kotlin
val availableKeywords: List<PersonaKeyword> = listOf(
    // 기존 키워드...
    PersonaKeyword("새 카테고리", "새 키워드", "프롬프트 문장 조각")
)
```

## 주의사항

1. **Gradle 캐시 문제 발생 시:**
   ```bash
   ./gradlew clean
   rm -rf .gradle/buildOutputCleanup/
   ```

2. **BuildConfig 에러 발생 시:**
   - `local.properties`에 `openaiApiKey`가 올바르게 설정되어 있는지 확인
   - API 키에 특수문자나 따옴표가 포함되지 않았는지 확인

3. **실시간 반영이 안 될 시:**
   - `SystemPromptManager`가 `@Singleton`으로 올바르게 주입되었는지 확인
   - `ChatRepositoryImpl`의 init 블록에서 Flow 구독이 정상 동작하는지 확인

