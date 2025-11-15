# Home 디렉토리 파일 구조 및 기능 설명

## 📁 디렉토리 구조

```
home/
├── adapter/
│   ├── holder/
│   │   ├── EmptyViewHolder.kt
│   │   └── SessionCardViewHolder.kt
│   ├── item/
│   │   └── HomeItem.kt
│   └── HomeAdapter.kt
├── binding/
│   └── HomeBindings.kt
├── navigation/
│   └── HomeNavigator.kt
├── HomeComponents.kt
├── HomeFragment.kt
├── HomeFragmentSimple.kt
├── HomeIntent.kt
├── HomeScreen.kt
├── HomeUiState.kt
├── HomeViewModel.kt
└── SessionData.kt
```

---

## 📄 파일별 기능 설명

### 🎯 핵심 파일

#### **HomeFragment.kt**
- **역할**: Home 화면의 메인 Fragment (XML 기반)
- **주요 기능**:
  - RecyclerView를 통한 세션 목록 표시
  - 검색 기능 (300ms debounce 적용)
  - FAB(Floating Action Button)을 통한 새 세션 생성
  - ViewModel과 연동하여 상태 관리
  - Navigation을 통한 화면 전환
- **아키텍처**: MVI 패턴 (Intent → ViewModel → State)
- **의존성**: HomeViewModel, HomeAdapter, HomeNavigator

#### **HomeFragmentSimple.kt**
- **역할**: 임시 테스트용 Simple Fragment
- **주요 기능**:
  - TextView만 사용하는 최소한의 UI
  - 빌드 에러 없이 작동 확인용
- **사용 상황**: 초기 개발/디버깅 단계에서 사용

#### **HomeScreen.kt** ⭐ NEW (Compose)
- **역할**: Jetpack Compose 기반의 Home 화면
- **주요 기능**:
  - 세션 목록을 Compose UI로 표시
  - HomeHeader, NewChatBanner, SearchBar 등 컴포넌트 조합
  - 빈 상태(EmptyState) 처리
- **특징**: 
  - XML Fragment 방식의 대안으로 추가된 Compose 버전
  - MainScreen.kt에서 호출됨

#### **HomeComponents.kt** ⭐ NEW (Compose)
- **역할**: 재사용 가능한 Compose UI 컴포넌트 모음
- **포함된 컴포넌트**:
  - `HomeHeader()` - LogTalk 로고와 아이콘 버튼 헤더
  - `NewChatBanner()` - 새 대화 시작 배너 (그라데이션 ChatGPT 아이콘)
  - `ChatGPTIconWithGradient()` - 그라데이션 효과가 적용된 ChatGPT 아이콘
  - `SearchBar()` - 상담 기록 검색 바
  - `SessionList()` - 세션 목록 LazyColumn
  - `SessionCard()` - 개별 세션 카드 UI
  - `EmptyState()` - 빈 상태 화면
- **특징**: 모든 Home 화면의 UI 컴포넌트가 분리되어 있어 재사용성 높음

---

### 🎨 ViewModel & State 관련

#### **HomeViewModel.kt**
- **역할**: Home 화면의 비즈니스 로직 및 상태 관리
- **주요 기능**:
  - UseCase를 통한 데이터 로드 (최근 세션, 검색, 세션 생성)
  - Intent 처리 (검색, 카드 클릭, FAB 클릭, Pull-to-Refresh)
  - UI State 관리 (Loading, Empty, Content, Error)
  - Navigation Event 발행
- **아키텍처**: MVI 패턴
- **의존성**: Hilt DI, Domain UseCase

#### **HomeUiState.kt**
- **역할**: Home 화면의 UI 상태 정의
- **상태 종류** (예상):
  - `Loading` - 로딩 중
  - `Empty` - 빈 상태
  - `Content(items)` - 컨텐츠 표시
  - `Error(message)` - 에러 발생
- **특징**: Sealed Class/Interface로 구현 (타입 안전성)

#### **HomeIntent.kt**
- **역할**: 사용자 액션(Intent) 정의
- **Intent 종류** (코드 기반):
  - `SearchChanged(query)` - 검색어 변경
  - `CardClicked(sessionId)` - 세션 카드 클릭
  - `FabClicked` - FAB 버튼 클릭
  - `PullToRefresh` - 새로고침
- **특징**: Sealed Class/Interface로 구현

---

### 📊 Data & Model

#### **SessionData.kt** ⭐ NEW
- **역할**: Compose에서 사용하는 세션 데이터 클래스
- **구조**:
  ```kotlin
  data class SessionData(
      val id: Long,
      val title: String,
      val lastMessage: String,
      val timeAgo: String
  )
  ```
- **사용처**: HomeScreen.kt, HomeComponents.kt

---

### 🔄 Adapter 관련 (RecyclerView)

#### **adapter/HomeAdapter.kt**
- **역할**: RecyclerView의 ListAdapter
- **주요 기능**:
  - DiffUtil을 통한 효율적인 리스트 업데이트
  - 다양한 ViewType 처리 (Header, Session, Empty)
  - ViewHolder 생성 및 바인딩
- **특징**: ListAdapter + DiffUtil 패턴

#### **adapter/item/HomeItem.kt**
- **역할**: RecyclerView 아이템 타입 정의
- **아이템 종류** (예상):
  - `HeaderItem` - 헤더 (FAB 안내 배너)
  - `SessionItem` - 세션 카드
  - `EmptyItem` - 빈 상태
- **특징**: Sealed Class로 구현

#### **adapter/holder/SessionCardViewHolder.kt**
- **역할**: 세션 카드 ViewHolder
- **주요 기능**:
  - 세션 제목, 마지막 메시지, 시간 표시
  - 클릭 이벤트 처리
  - HomeBindings의 helper 함수 사용 (상대 시간 포맷)

#### **adapter/holder/EmptyViewHolder.kt**
- **역할**: 빈 상태 ViewHolder
- **주요 기능**:
  - 정적 문구/아이콘 표시
  - 바인딩 로직 최소화

---

### 🔧 Utility & Helper

#### **binding/HomeBindings.kt**
- **역할**: DataBinding/ViewBinding 관련 헬퍼 함수
- **주요 기능** (예상):
  - `formatRelativeTime()` - 상대 시간 포맷팅 ("54분 전", "1일 전" 등)
  - 기타 UI 바인딩 헬퍼 함수
- **특징**: 파일이 비어있을 수 있음 (구현 중)

#### **navigation/HomeNavigator.kt**
- **역할**: Home 화면의 Navigation 인터페이스
- **주요 기능** (예상):
  - `toChat(sessionId: Long)` - 채팅 화면으로 이동
- **특징**: 
  - Fragment가 구현하는 인터페이스
  - Navigation Component와 연동

---

## 🏗️ 아키텍처 패턴

### MVI (Model-View-Intent)
```
User Action (Intent) 
    → HomeViewModel.sendIntent()
    → observeIntents()
    → Business Logic (UseCase)
    → _uiState.value = NewState
    → HomeFragment.collectState()
    → UI Update
```

### 데이터 흐름
```
Domain Layer (UseCase)
    ↓
HomeViewModel (State Management)
    ↓
HomeFragment (UI Rendering)
    ↓
HomeAdapter (RecyclerView)
    ↓
ViewHolder (Item Binding)
```

---

## 🎯 두 가지 UI 구현 방식

### 1. **XML 기반 (기존)**
- HomeFragment.kt
- HomeAdapter.kt + ViewHolder
- XML Layout 파일 사용
- DataBinding/ViewBinding

### 2. **Compose 기반 (신규)** ⭐
- HomeScreen.kt
- HomeComponents.kt
- SessionData.kt
- Jetpack Compose UI
- 더 간결하고 선언적인 UI

---

## 📝 주요 기능 요약

| 기능 | 관련 파일 | 설명 |
|-----|---------|------|
| **세션 목록 표시** | HomeFragment, HomeScreen | 최근 세션 목록을 스크롤 가능하게 표시 |
| **검색** | HomeFragment, SearchBar | 300ms debounce 적용된 실시간 검색 |
| **새 세션 생성** | FAB, NewChatBanner | 버튼 클릭 시 새 세션 생성 후 채팅 화면 이동 |
| **세션 상세 보기** | SessionCard, CardClicked | 세션 카드 클릭 시 해당 채팅 화면으로 이동 |
| **빈 상태 처리** | EmptyState, EmptyViewHolder | 세션이 없을 때 안내 메시지 표시 |
| **상대 시간 표시** | HomeBindings | "54분 전", "1일 전" 형식으로 시간 표시 |

---

## 🚀 사용 예시

### Compose 방식 (MainScreen.kt에서 호출)
```kotlin
composable(MainScreenRoutes.Home.route) { 
    HomeScreen() 
}
```

### Fragment 방식 (Navigation Graph에서 사용)
```kotlin
<fragment
    android:id="@+id/homeFragment"
    android:name="com.example.logtalk.ui.home.HomeFragment"
    tools:layout="@layout/fragment_home" />
```

---

## 💡 개선 제안

1. **HomeIntent.kt, HomeUiState.kt** - 현재 비어있음, 구현 필요
2. **HomeBindings.kt** - 헬퍼 함수 구현 필요
3. **HomeNavigator.kt** - 인터페이스 정의 필요
4. **HomeItem.kt** - Sealed Class 정의 필요
5. **두 UI 방식 통합** - XML과 Compose 중 하나로 통일 고려

---

**작성일**: 2025년 11월 13일  
**프로젝트**: LogTalk - 객체지향프로그래밍 2025-2

