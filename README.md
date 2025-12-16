```
com.example.logtalk
├── core
│   ├── di             (Dependency Injection 설정)
│   ├── firebase       (미래 확장 시 Firebase 관련 모듈)
│   ├── network        (Retrofit 등 API 통신 설정)
│   └── utils          (공통 유틸리티 함수)
├── data
│   ├── local          (Room DB 관련: DAO, Database, Entity, TypeConverters) DB 구현
│   ├── remote         (API 인터페이스 및 데이터 소스)
│   └── repositoryImpl (Domain 계층 Repository의 실제 구현체)
├── domain
│   ├── model          (도메인 모델 정의)
│   ├── repository     (데이터 소스 추상화 인터페이스)
│   └── usecase        (비즈니스 로직 및 UseCase 정의)
└── ui
    ├── chat           (메시지 화면)
    ├── home           (홈/상담 목록 화면)
    ├── settings       (설정 화면)
    └── summary        (최근 상태 요약 화면)

```
