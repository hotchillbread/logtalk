#!/bin/zsh

# Gradle 캐시 손상 문제 해결 스크립트
# 에러: kotlin-dsl/accessors metadata.bin 파일을 찾을 수 없음

echo "🔧 Gradle 캐시 손상 문제 해결 시작..."
echo ""
echo "에러: /Users/na-gyeong/.gradle/caches/8.13/kotlin-dsl/accessors/.../metadata.bin"
echo "원인: Gradle 캐시 파일이 손상되었습니다."
echo ""

cd "$(dirname "$0")"

# 1단계: 프로젝트 빌드 폴더 정리
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "1️⃣  프로젝트 빌드 폴더 정리 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

folders_to_clean=(
    "app/build"
    "build"
    ".gradle"
    ".idea/caches"
)

for folder in "${folders_to_clean[@]}"; do
    if [ -d "$folder" ]; then
        echo "  🗑️  삭제: $folder"
        rm -rf "$folder"
        echo "     ✅ 완료"
    else
        echo "  ℹ️  없음: $folder"
    fi
done

echo ""

# 2단계: 사용자 Gradle 캐시 정리 (핵심!)
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "2️⃣  Gradle 전역 캐시 정리 중... (핵심!)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

gradle_dirs=(
    "$HOME/.gradle/caches"
    "$HOME/.gradle/daemon"
    "$HOME/.gradle/kotlin-dsl"
)

for dir in "${gradle_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "  🗑️  삭제: $dir"
        rm -rf "$dir"
        echo "     ✅ 완료"
    else
        echo "  ℹ️  없음: $dir"
    fi
done

echo ""

# 3단계: Android Studio 캐시 정리
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "3️⃣  Android Studio 캐시 정리 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Android Studio 캐시 폴더들
studio_cache_dirs=(
    "$HOME/Library/Caches/Google/AndroidStudio"*
    "$HOME/Library/Application Support/Google/AndroidStudio"*/caches
)

for pattern in "${studio_cache_dirs[@]}"; do
    if ls -d $pattern 2>/dev/null; then
        echo "  🗑️  삭제: Android Studio 캐시"
        rm -rf $pattern
        echo "     ✅ 완료"
    else
        echo "  ℹ️  없음: Android Studio 캐시"
    fi
done

echo ""

# 4단계: Gradle Wrapper 재검증
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "4️⃣  Gradle Wrapper 검증 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -f "gradlew" ]; then
    echo "  ✅ gradlew 파일 존재"
    chmod +x gradlew
    echo "  ✅ 실행 권한 부여 완료"
else
    echo "  ⚠️  gradlew 파일 없음"
fi

echo ""

# 5단계: macOS 메타데이터 정리
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "5️⃣  macOS 메타데이터 정리 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

find . -name ".DS_Store" -type f -delete 2>/dev/null
echo "  ✅ .DS_Store 파일 정리 완료"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✨ 정리 완료!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📱 다음 단계:"
echo ""
echo "  방법 1: Android Studio 사용 (권장)"
echo "    1. Android Studio를 완전히 종료하세요"
echo "    2. Android Studio를 다시 시작하세요"
echo "    3. File > Invalidate Caches / Restart 선택"
echo "    4. Invalidate and Restart 클릭"
echo "    5. 프로젝트가 열리면 자동으로 Gradle Sync 시작"
echo ""
echo "  방법 2: 터미널에서 바로 Sync (빠름)"
echo "    ./gradlew clean"
echo "    ./gradlew --refresh-dependencies"
echo ""
echo "💡 Tip: Android Studio를 재시작하는 것이 가장 확실합니다!"
echo ""

