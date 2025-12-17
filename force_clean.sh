#!/bin/bash

echo "🔥 강제 빌드 정리 시작..."
echo ""

cd "$(dirname "$0")"

# Gradle 데몬 종료
echo "1️⃣ Gradle 데몬 종료 중..."
./gradlew --stop 2>/dev/null || true
echo "   ✅ Gradle 데몬 종료 완료"
echo ""

# 모든 빌드 디렉토리 삭제
echo "2️⃣ 빌드 디렉토리 삭제 중..."

if [ -d "app/build" ]; then
    rm -rf app/build
    echo "   ✅ app/build 삭제"
fi

if [ -d "build" ]; then
    rm -rf build
    echo "   ✅ build 삭제"
fi

if [ -d ".gradle" ]; then
    rm -rf .gradle
    echo "   ✅ .gradle 삭제"
fi

if [ -d "app/.gradle" ]; then
    rm -rf app/.gradle
    echo "   ✅ app/.gradle 삭제"
fi

# .kotlin 디렉토리도 삭제
if [ -d ".kotlin" ]; then
    rm -rf .kotlin
    echo "   ✅ .kotlin 삭제"
fi

echo ""
echo "3️⃣ Gradle clean 실행 중..."
./gradlew clean
echo "   ✅ Clean 완료"

echo ""
echo "✨ 정리 완료!"
echo ""
echo "이제 다음 명령어로 빌드하세요:"
echo "  ./gradlew assembleDebug"
echo ""
echo "또는 Android Studio에서:"
echo "  Build > Rebuild Project"
echo ""

