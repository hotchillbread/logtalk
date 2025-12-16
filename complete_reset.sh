#!/bin/zsh

# Android Studio 완전 초기화 스크립트
# 이 스크립트는 모든 캐시와 빌드 파일을 삭제하고 프로젝트를 깨끗한 상태로 만듭니다.

echo "🧹 Android Studio 완전 초기화 시작..."
echo ""
echo "⚠️  주의: 이 작업은 모든 빌드 결과물과 캐시를 삭제합니다."
echo ""

read "response?계속하시겠습니까? (y/N): "
if [[ ! "$response" =~ ^[Yy]$ ]]; then
    echo "취소되었습니다."
    exit 0
fi

cd "$(dirname "$0")"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🗑️  빌드 파일 삭제 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# 프로젝트 빌드 폴더
folders_to_delete=(
    "app/build"
    "build"
    ".gradle"
    ".idea/caches"
    ".idea/libraries"
    "app/.cxx"
    "app/.externalNativeBuild"
)

for folder in "${folders_to_delete[@]}"; do
    if [ -d "$folder" ]; then
        echo "  🗑️  삭제 중: $folder"
        rm -rf "$folder"
        echo "     ✅ 완료"
    else
        echo "  ℹ️  없음: $folder"
    fi
done

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🧹 시스템 캐시 정리 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Gradle 전역 캐시
if [ -d "$HOME/.gradle/caches" ]; then
    echo "  🗑️  Gradle 캐시 삭제 중..."
    rm -rf "$HOME/.gradle/caches"
    echo "     ✅ 완료"
fi

# Android Studio 캐시 (선택적)
# if [ -d "$HOME/Library/Caches/Google/AndroidStudio*" ]; then
#     echo "  🗑️  Android Studio 캐시 삭제 중..."
#     rm -rf "$HOME/Library/Caches/Google/AndroidStudio"*
#     echo "     ✅ 완료"
# fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🧼 macOS 메타데이터 정리 중..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# .DS_Store 파일
echo "  🗑️  .DS_Store 파일 삭제 중..."
find . -name ".DS_Store" -type f -delete 2>/dev/null
echo "     ✅ 완료"

# 공백이 포함된 파일 검색
echo ""
echo "  🔍 공백이 포함된 파일 검색 중..."
found=0
while IFS= read -r -d '' file; do
    if [[ "$file" == *" "* ]] && [[ "$file" == *.xml ]]; then
        echo "     ⚠️  발견: $file"
        rm -f "$file"
        echo "        ✅ 삭제됨"
        found=1
    fi
done < <(find app/src/main/res -type f -print0 2>/dev/null)

if [ $found -eq 0 ]; then
    echo "     ✅ 문제 파일 없음"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✨ 초기화 완료!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📱 다음 단계:"
echo ""
echo "  1. Android Studio를 완전히 종료하세요"
echo "  2. Android Studio를 다시 시작하세요"
echo "  3. 프로젝트를 다시 열면 자동으로 Gradle Sync가 시작됩니다"
echo "  4. Sync 완료 후 'Build > Rebuild Project' 실행"
echo "  5. 앱 실행 ▶️"
echo ""
echo "💡 추가 옵션 (문제가 계속되면):"
echo "    File > Invalidate Caches / Restart"
echo ""

