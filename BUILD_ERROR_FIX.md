# 빌드 에러 해결 방법

## 문제 상황
```
Failed file name validation for file .../drawable/ic_chatgpt 2.xml
' ' is not a valid file-based resource name character
```

이 에러는 Android 리소스 파일명에 공백이 포함되어 빌드가 실패하는 문제입니다.
파일명 `ic_chatgpt 2.xml`에 공백(" ")이 있어서 발생했습니다.

## 원인
- 빌드 캐시에 잘못된 중간 파일이 생성됨
- macOS에서 파일 복사 시 자동으로 " 2"가 추가되는 경우가 있음

## 해결 방법

### 방법 1: Android Studio에서 (가장 간단)

1. **Build** 메뉴 클릭
2. **Clean Project** 선택
3. 완료 후 **Build > Rebuild Project** 선택

### 방법 2: 터미널에서

```bash
# 프로젝트 폴더로 이동
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# clean_build.sh 스크립트 실행 권한 부여
chmod +x clean_build.sh

# 스크립트 실행
./clean_build.sh
```

### 방법 3: 수동으로 Gradle 명령어 실행

```bash
cd /Users/na-gyeong/Desktop/object_programing-2025-2

# 빌드 폴더 정리
./gradlew clean

# 또는 모든 빌드 캐시 삭제
rm -rf app/build build .gradle

# 다시 빌드
./gradlew assembleDebug
```

### 방법 4: Finder에서 직접 삭제

1. Finder에서 프로젝트 폴더 열기
2. 다음 폴더들을 삭제:
   - `app/build`
   - `build`
   - `.gradle` (숨김 폴더 - Cmd+Shift+. 로 보기)
3. Android Studio로 돌아가서 **File > Invalidate Caches / Restart** 선택

## 확인 사항

빌드 에러 해결 후 다음을 확인하세요:

- ✅ 모든 리소스 파일명에 공백이 없는지
- ✅ 파일명은 소문자, 숫자, 언더스코어(_)만 사용
- ✅ 중복된 파일이 없는지 (예: `file.xml`, `file 2.xml`)

## 추가 도움말

만약 위 방법으로도 해결되지 않는다면:

1. Android Studio 재시작
2. 컴퓨터 재부팅
3. Android Studio 캐시 무효화:
   - **File > Invalidate Caches / Restart**
   - **Invalidate and Restart** 선택

## 참고

이 문제는 소스 코드의 문제가 아니라 빌드 시스템의 캐시 문제입니다.
빌드 폴더를 정리하면 대부분 해결됩니다.

