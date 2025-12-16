# 🚨 Gradle 캐시 손상 에러 해결

## 에러 메시지
```
/Users/na-gyeong/.gradle/caches/8.13/kotlin-dsl/accessors/45fa3cb6b4077a86ce0b8bff6eabdd7d/metadata.bin
(No such file or directory)
```

## 🔍 문제 원인

이 에러는 Gradle의 Kotlin DSL 캐시가 손상되었을 때 발생합니다:

1. **Gradle 캐시 손상**: 빌드 중 중단, 디스크 공간 부족, 권한 문제 등
2. **버전 충돌**: Gradle 버전 변경 후 캐시 미갱신
3. **파일 시스템 문제**: macOS 파일 시스템 에러

---

## ✅ 해결 방법

### 🥇 방법 1: 올인원 스크립트 (가장 빠름!)

**한 번에 모든 문제를 해결합니다:**

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# 실행 권한 부여
chmod +x fix_all_build_issues.sh

# 스크립트 실행
./fix_all_build_issues.sh
```

이 스크립트는 자동으로:
- ✅ 프로젝트 빌드 폴더 정리
- ✅ **Gradle 전역 캐시 완전 삭제** (핵심!)
- ✅ Android Studio 캐시 정리
- ✅ 문제 파일 검색 및 삭제
- ✅ Gradle Wrapper 검증

---

### 🥈 방법 2: Gradle 캐시만 삭제

**문제가 Gradle 캐시 손상만인 경우:**

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# 스크립트 실행
chmod +x fix_gradle_cache.sh
./fix_gradle_cache.sh
```

---

### 🥉 방법 3: 수동 삭제

**터미널에서 직접:**

```bash
# 1. 프로젝트 캐시 삭제
cd /Users/na-gyeong/Desktop/object_programing-2025-2
rm -rf .gradle
rm -rf build
rm -rf app/build

# 2. Gradle 전역 캐시 삭제 (핵심!)
rm -rf ~/.gradle/caches
rm -rf ~/.gradle/daemon
rm -rf ~/.gradle/kotlin-dsl

# 3. Android Studio 캐시 삭제
rm -rf ~/Library/Caches/Google/AndroidStudio*

# 4. Gradle Clean 실행
./gradlew clean --refresh-dependencies
```

---

## 🔄 완료 후 단계

### A. Android Studio 사용 (권장 ⭐)

1. **Android Studio 완전 종료**
2. **Android Studio 재시작**
3. **File** → **Invalidate Caches / Restart**
4. **Invalidate and Restart** 클릭
5. 프로젝트가 자동으로 Gradle Sync 시작 (시간 소요)
6. Sync 완료 대기
7. **Build** → **Rebuild Project**
8. 앱 실행 ▶️

### B. 터미널 사용 (빠름 🚀)

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# Gradle 의존성 새로고침
./gradlew clean --refresh-dependencies

# 빌드
./gradlew build

# 또는 Android Studio에서 바로 실행
```

---

## 📊 예상 소요 시간

| 단계 | 시간 |
|------|------|
| 캐시 삭제 | 30초 |
| Gradle Sync | 2-5분 (인터넷 속도에 따라) |
| Rebuild | 1-3분 |
| **총 예상** | **5-10분** |

첫 Sync는 Gradle이 모든 파일을 다시 다운로드하므로 시간이 걸립니다.

---

## ⚠️ 주의사항

### 인터넷 연결 필수
- Gradle이 캐시를 재생성하려면 인터넷이 필요합니다
- 의존성 파일들을 Maven/Google Repository에서 다운로드합니다

### 충분한 디스크 공간
- 최소 3-5GB의 여유 공간 필요
- Gradle 캐시가 재생성되면서 공간 사용

### VPN/방화벽
- VPN 사용 중이면 일시 해제
- 방화벽이 Gradle 다운로드를 차단하지 않는지 확인

---

## 🛠️ 문제가 계속되면?

### 1. Gradle Wrapper 재생성

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# Gradle Wrapper 삭제
rm -rf gradle/wrapper

# Android Studio에서 File > Sync Project를 실행하면
# Gradle Wrapper가 자동으로 재생성됩니다
```

### 2. 컴퓨터 재부팅

간혹 프로세스나 파일 잠금 문제로 해결이 안 될 수 있습니다:
1. Android Studio 완전 종료
2. Mac 재부팅
3. 위의 스크립트 다시 실행
4. Android Studio 시작

### 3. Android Studio 재설치

극단적인 경우:
1. Android Studio 완전 삭제
2. `~/Library/Application Support/Google/AndroidStudio*` 삭제
3. `~/Library/Caches/Google/AndroidStudio*` 삭제
4. Android Studio 최신 버전 다운로드 및 설치
5. SDK 재설치

---

## 📋 체크리스트

빌드 전 확인사항:

- [ ] 인터넷 연결 확인
- [ ] 디스크 공간 충분 (3GB 이상)
- [ ] Android Studio 완전 종료 후 재시작
- [ ] Gradle 캐시 삭제 완료
- [ ] Invalidate Caches 실행
- [ ] VPN/방화벽 확인

---

## 🎯 권장 해결 순서

```
1. fix_all_build_issues.sh 실행
   ↓
2. Android Studio 재시작
   ↓
3. File > Invalidate Caches / Restart
   ↓
4. Gradle Sync 완료 대기 (2-5분)
   ↓
5. Build > Rebuild Project
   ↓
6. 앱 실행 ▶️
```

---

## 💡 예방 방법

앞으로 이런 문제를 방지하려면:

1. **정기적인 Clean**
   ```bash
   ./gradlew clean  # 주 1회
   ```

2. **빌드 중단 주의**
   - Gradle Sync 중에는 Android Studio를 강제 종료하지 말 것
   - 빌드 중에는 컴퓨터를 끄지 말 것

3. **충분한 디스크 공간 유지**
   - 최소 10GB 이상 여유 공간 유지

4. **Gradle 캐시 주기적 정리**
   ```bash
   # 월 1회 실행
   rm -rf ~/.gradle/caches
   ```

---

## 📞 여전히 문제가 있나요?

**다음 정보를 확인해주세요:**

1. Gradle 버전:
   ```bash
   ./gradlew --version
   ```

2. 디스크 공간:
   ```bash
   df -h
   ```

3. 에러 로그:
   - Android Studio: View > Tool Windows > Build
   - 터미널: `./gradlew build --stacktrace`

---

## ✨ 요약

**가장 빠른 해결책:**

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2
chmod +x fix_all_build_issues.sh
./fix_all_build_issues.sh
```

**그 다음:**
- Android Studio 재시작
- Invalidate Caches / Restart
- Gradle Sync 완료 대기
- Rebuild Project
- 앱 실행!

**5-10분이면 해결됩니다!** 🎉

