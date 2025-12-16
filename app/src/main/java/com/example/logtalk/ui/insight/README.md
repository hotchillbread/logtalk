# 인사이트 기능 (Insight Feature)

## 개요
사용자의 상담 데이터를 분석하여 통계와 감정 변화 그래프를 제공하는 기능입니다.

## 파일 구조
```
ui/insight/
├── InsightScreen.kt          # 메인 화면
├── InsightViewModel.kt       # 뷰모델 (데이터 관리)
└── components/
    ├── InsightComponents.kt  # 헤더, 통계 카드, 채팅 요약 카드
    └── EmotionGraph.kt       # 감정 그래프 컴포넌트
```

## 주요 기능

### 1. 통계 정보
- **총 상담 수**: 사용자가 진행한 총 상담 세션 수
- **총 메시지**: 전체 메시지 개수 (사용자 + 봇)

### 2. 전체 인사이트 그래프
- X축: 날짜 (최근 7일)
- Y축: 긍정 감정 퍼센트 (0-100%)
- 감정 변화 추세 표시 (상승/하락/유지)
- 그라데이션 영역 그래프로 시각화

### 3. 최근 상담 요약
- 채팅 제목 (GPT가 실시간 요약)
- 봇 답변 요약
- 날짜 및 메시지 개수

## 데이터 모델

### InsightUiState
```kotlin
data class InsightUiState(
    val totalChats: Int,              // 총 상담 수
    val totalMessages: Int,           // 총 메시지 수
    val emotionData: List<EmotionDataPoint>,  // 감정 데이터
    val emotionTrend: String?,        // 감정 추세 메시지
    val recentChats: List<ChatSummary>  // 최근 채팅 요약
)
```

### EmotionDataPoint
```kotlin
data class EmotionDataPoint(
    val date: String,                 // 날짜 (M/d 형식)
    val positivePercentage: Float     // 긍정 감정 비율 (0-100)
)
```

### ChatSummary
```kotlin
data class ChatSummary(
    val title: String,                // 채팅 제목 (GPT 요약)
    val summary: String,              // 답변 요약
    val date: String,                 // 날짜
    val messageCount: Int             // 메시지 수
)
```

## 네비게이션
- 홈 화면 우측 상단 인사이트 아이콘(BarChart) 클릭 시 이동
- 뒤로가기 버튼으로 홈 화면 복귀
- 하단 네비게이션 바의 홈 버튼으로도 복귀 가능

## 데이터 소스
- **총 상담 수**: `Title` 테이블의 레코드 수
- **총 메시지**: 모든 `MessageData`의 총 개수
- **최근 상담 요약**: 최근 3개의 `Title` + 각 첫 번째 메시지
- **데이터 없음**: 데이터베이스에 채팅 데이터가 없으면 빈 화면 표시

## TODO
- [x] 실제 데이터베이스 연동 완료
- [x] 데이터 없을 때 빈 화면 표시
- [ ] GPT를 통한 채팅 내용 분석 및 감정 분석
- [ ] 채팅 요약 자동 생성 (현재는 첫 메시지만 표시)
- [ ] 감정 데이터 저장 및 불러오기 (현재는 더미 데이터)
- [ ] 그래프 상호작용 기능 (터치 시 상세 정보 표시)

