#!/bin/bash

# Android 프로젝트 빌드 정리 스크립트

echo "🧹 빌드 폴더 정리 시작..."

cd "$(dirname "$0")"

# build 폴더 삭제
if [ -d "app/build" ]; then
    echo "app/build 폴더 삭제 중..."
    rm -rf app/build
    echo "✅ app/build 폴더 삭제 완료"
fi

if [ -d "build" ]; then
    echo "build 폴더 삭제 중..."
    rm -rf build
    echo "✅ build 폴더 삭제 완료"
fi

# .gradle 캐시 정리
if [ -d ".gradle" ]; then
    echo ".gradle 캐시 정리 중..."
    rm -rf .gradle
    echo "✅ .gradle 캐시 정리 완료"
fi

echo ""
echo "✨ 정리 완료! 이제 프로젝트를 다시 빌드하세요."
echo ""
echo "Android Studio에서:"
echo "  Build > Clean Project"
echo "  Build > Rebuild Project"
echo ""
echo "또는 터미널에서:"
echo "  ./gradlew clean"
echo "  ./gradlew assembleDebug"

