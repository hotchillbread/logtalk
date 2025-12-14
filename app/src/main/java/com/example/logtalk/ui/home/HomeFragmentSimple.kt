package com.example.logtalk.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * 임시 Simple HomeFragment - 빌드 에러 없이 작동하는 버전
 */
class HomeFragmentSimple : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext()).apply {
            text = "홈 화면 테스트\n\n" +
                    "세션 목록이 여기에 표시됩니다.\n\n" +
                    "현재 기본 버전으로 실행 중입니다."
            textSize = 18f
            setPadding(50, 50, 50, 50)
        }
    }
}

