package com.example.logtalk.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Home 화면 - 실제 디자인 구현
@Composable
fun HomeScreen() {
    // 임시 세션 데이터
    val sessions = remember {
        listOf(
            SessionData(1, "너 생각에는 내가 재수강을 하는 게 좋을까?", "아 그러냐 넌 왜 그렇게 생각해? 3줄로 말해", "54분 전"),
            SessionData(2, "너 생각에는 내가 재수강을 하는 게 좋을까?", "아 그러냐 넌 왜 그렇게 생각해? 3줄로 말해", "1일 전"),
            SessionData(3, "너 생각에는 내가 재수강을 하는 게 좋을까?", "아 그러냐 넌 왜 그렇게 생각해? 3줄로 말해", "11월 1일"),
            SessionData(4, "너 생각에는 내가 재수강을 하는 게 좋을까?", "아 그러냐 넌 왜 그렇게 생각해? 3줄로 말해", "10월 29일")
        )
    }

    var showEmptyState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 헤더
        HomeHeader()

        // 상담 시작 배너
        NewChatBanner(onClick = { /* TODO: 새 상담 시작 */ })

        // 검색 바
        SearchBar()

        Spacer(modifier = Modifier.height(8.dp))

        // 세션 목록 또는 빈 상태
        if (showEmptyState || sessions.isEmpty()) {
            EmptyState()
        } else {
            SessionList(sessions = sessions)
        }
    }
}
