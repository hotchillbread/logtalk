#!/bin/bash

# macOS 메타데이터 및 중복 파일 정리 스크립트

echo "🔍 프로젝트에서 문제가 될 수 있는 파일 검색 중..."

cd "$(dirname "$0")"

# .DS_Store 파일 찾기 및 삭제
echo ""
echo "📁 .DS_Store 파일 검색..."
find . -name ".DS_Store" -type f -delete 2>/dev/null
echo "✅ .DS_Store 파일 정리 완료"

# 공백이 포함된 파일 찾기
echo ""
echo "🔎 공백이 포함된 리소스 파일 검색..."
found_space_files=false
while IFS= read -r -d '' file; do
    if [[ "$file" == *" "* ]]; then
        echo "⚠️  발견: $file"
        found_space_files=true
    fi
done < <(find app/src/main/res -type f -print0 2>/dev/null)

if [ "$found_space_files" = false ]; then
    echo "✅ 공백이 포함된 파일 없음"
fi

# " 2" 패턴이 있는 파일 찾기 (macOS 복사본)
echo ""
echo "🔎 macOS 복사본 파일 검색 (예: file 2.xml)..."
found_copy_files=false
while IFS= read -r file; do
    if [[ "$file" == *" 2."* ]] || [[ "$file" == *" 3."* ]]; then
        echo "⚠️  발견: $file"
        found_copy_files=true
        # 자동 삭제 옵션 (주석 해제하여 사용)
        # rm "$file"
        # echo "   -> 삭제됨"
    fi
done < <(find app/src/main/res -type f 2>/dev/null)

if [ "$found_copy_files" = false ]; then
    echo "✅ 복사본 파일 없음"
else
    echo ""
    echo "💡 위 파일들을 수동으로 삭제하거나, 이 스크립트를 수정하여 자동 삭제할 수 있습니다."
fi

# 빌드 폴더의 문제 파일 검색
echo ""
echo "🔎 빌드 폴더의 문제 파일 검색..."
if [ -d "app/build" ]; then
    found_build_issues=false
    while IFS= read -r file; do
        if [[ "$file" == *" "* ]]; then
            echo "⚠️  발견: $file"
            found_build_issues=true
        fi
    done < <(find app/build -name "*.xml" 2>/dev/null)

    if [ "$found_build_issues" = false ]; then
        echo "✅ 빌드 폴더에 문제 파일 없음"
    else
        echo ""
        echo "💡 빌드 폴더를 정리하려면 clean_build.sh를 실행하세요."
    fi
else
    echo "ℹ️  빌드 폴더 없음"
fi

echo ""
echo "✨ 검사 완료!"
echo ""
echo "다음 단계:"
echo "  1. 문제가 있는 파일이 발견되면 수동으로 삭제"
echo "  2. ./clean_build.sh 실행하여 빌드 폴더 정리"
echo "  3. Android Studio에서 프로젝트 다시 빌드"

