#!/bin/zsh

# 모든 해결 스크립트에 실행 권한 부여

echo "🔑 모든 스크립트에 실행 권한 부여 중..."
echo ""

cd "$(dirname "$0")"

scripts=(
    "fix_all_build_issues.sh"
    "fix_gradle_cache.sh"
    "fix_build_error.sh"
    "complete_reset.sh"
    "clean_build.sh"
    "check_problematic_files.sh"
)

for script in "${scripts[@]}"; do
    if [ -f "$script" ]; then
        chmod +x "$script"
        echo "  ✅ $script"
    else
        echo "  ⚠️  $script (파일 없음)"
    fi
done

echo ""
echo "✨ 완료!"
echo ""
echo "📋 사용 가능한 스크립트:"
echo ""
echo "  🔧 올인원 해결 (권장):"
echo "     ./fix_all_build_issues.sh"
echo ""
echo "  🎯 특정 문제:"
echo "     ./fix_gradle_cache.sh        # Gradle 캐시 손상"
echo "     ./fix_build_error.sh         # 일반 빌드 에러"
echo "     ./complete_reset.sh          # 완전 초기화"
echo ""
echo "  🧹 정리 도구:"
echo "     ./clean_build.sh             # 빌드 폴더만"
echo "     ./check_problematic_files.sh # 문제 파일 검사"
echo ""

