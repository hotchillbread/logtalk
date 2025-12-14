package com.example.logtalk.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.logtalk.R

/**
 * ChatFragment - 채팅 화면 (임시)
 * TODO: 실제 채팅 기능 구현
 */
class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 임시: activity_chat 레이아웃 재사용
        return inflater.inflate(R.layout.activity_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SafeArgs로 전달된 sessionId 받기
        val sessionId = arguments?.getLong("sessionId", 0L) ?: 0L

        // TODO: sessionId를 사용하여 해당 세션의 채팅 데이터 로드
    }
}

