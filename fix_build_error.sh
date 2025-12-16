#!/bin/zsh

# 즉시 빌드 문제 해결 스크립트
# 이 스크립트는 공백이 포함된 파일명 때문에 발생한 빌드 에러를 해결합니다.

echo "🔧 빌드 에러 자동 해결 시작..."
echo ""

cd "$(dirname "$0")"

# 1단계: 문제 파일 확인
echo "1️⃣  문제 파일 확인 중..."
if [ -f "app/build/intermediates/packaged_res/debug/packageDebugResources/drawable/ic_chatgpt 2.xml" ]; then
    echo "   ⚠️  문제 파일 발견: ic_chatgpt 2.xml"
    rm -f "app/build/intermediates/packaged_res/debug/packageDebugResources/drawable/ic_chatgpt 2.xml"
    echo "   ✅ 문제 파일 삭제 완료"
else
    echo "   ℹ️  특정 문제 파일 없음 (정상)"
fi

echo ""

# 2단계: 빌드 폴더 전체 정리
echo "2️⃣  빌드 폴더 정리 중..."
rm -rf app/build
rm -rf build
rm -rf .gradle
echo "   ✅ 빌드 폴더 정리 완료"

echo ""

# 3단계: Gradle 캐시 정리
echo "3️⃣  Gradle 캐시 정리 중..."
if [ -d "$HOME/.gradle/caches" ]; then
    rm -rf "$HOME/.gradle/caches"
    echo "   ✅ Gradle 캐시 정리 완료"
else
    echo "   ℹ️  Gradle 캐시 없음"
fi

echo ""

# 4단계: macOS 메타데이터 정리
echo "4️⃣  macOS 메타데이터 정리 중..."
find . -name ".DS_Store" -type f -delete 2>/dev/null
echo "   ✅ .DS_Store 파일 정리 완료"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✨ 정리 완료!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📱 이제 Android Studio에서 다음을 실행하세요:"
echo ""
echo "   1. File > Sync Project with Gradle Files"
echo "   2. Build > Rebuild Project"
echo "   3. 앱 실행 (▶️ 버튼)"
echo ""
echo "또는 터미널에서:"
echo ""
echo "   ./gradlew clean"
echo "   ./gradlew assembleDebug"
echo ""
echo "💡 Tip: Android Studio를 재시작하면 더 확실합니다!"
echo ""

