#!/bin/bash

echo "🔧 빌드 오류 수정 중..."
echo ""

cd "$(dirname "$0")"

# Gradle 프로세스 종료
echo "1. Gradle 프로세스 종료..."
./gradlew --stop 2>/dev/null
killall -9 java 2>/dev/null
echo "   ✅ 완료"
echo ""

# 문제의 디렉토리 삭제
echo "2. 중복 디렉토리 삭제..."
if [ -d "app/build/intermediates/classes/debug/transformDebugClassesWithAsm/dirs/com/example/logtalk 2" ]; then
    rm -rf "app/build/intermediates/classes/debug/transformDebugClassesWithAsm/dirs/com/example/logtalk 2"
    echo "   ✅ logtalk 2 디렉토리 삭제"
fi

# 전체 빌드 폴더 삭제
echo ""
echo "3. 전체 빌드 폴더 삭제..."
rm -rf app/build
rm -rf build
rm -rf .gradle
rm -rf .kotlin
rm -rf app/.gradle
echo "   ✅ 완료"

# Clean
echo ""
echo "4. Gradle clean 실행..."
./gradlew clean --no-daemon
echo "   ✅ 완료"

echo ""
echo "✨ 수정 완료!"
echo ""
echo "이제 빌드를 실행하세요:"
echo "  ./gradlew assembleDebug"
echo ""

