package com.example.logtalk.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logtalk.ui.home.binding.HomeBindings

// Home 화면 - 실제 디자인 구현
@Composable
fun HomeScreen(
    onGroomyClick: () -> Unit = {},
    onInsightClick: () -> Unit = {},
    onSessionClick: (Long) -> Unit = {},
    onNewChatClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // NavEvent 처리
    LaunchedEffect(Unit) {
        viewModel.navEvents.collect { event ->
            when (event) {
                is HomeViewModel.NavEvent.ToChat -> onSessionClick(event.sessionId)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 헤더
        HomeHeader(
            onGroomyClick = onGroomyClick,
            onInsightClick = onInsightClick
        )

        androidx.compose.material3.HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        // 상담 시작 배너
        NewChatBanner(onClick = onNewChatClick)

        androidx.compose.material3.HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        // 검색 바
        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                viewModel.sendIntent(HomeIntent.SearchChanged(query))
            }
        )

        androidx.compose.material3.HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상태에 따른 UI 표시
        when (uiState) {
            is HomeUiState.Loading -> {
                // 로딩 상태
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
            is HomeUiState.Empty -> {
                EmptyState(searchQuery = searchQuery)
            }
            is HomeUiState.Content -> {
                val content = uiState as HomeUiState.Content
                val sessions = content.items
                    .filterIsInstance<com.example.logtalk.ui.home.adapter.item.HomeItem.SessionItem>()
                    .map { item ->
                        SessionData(
                            id = item.id,
                            title = item.title,
                            lastMessage = item.lastMessage ?: "",
                            timeAgo = HomeBindings.formatRelativeTime(item.updatedAt)
                        )
                    }
                SessionList(
                    sessions = sessions,
                    onSessionClick = { sessionId ->
                        viewModel.sendIntent(HomeIntent.CardClicked(sessionId))
                    },
                    onSessionDelete = { sessionId ->
                        viewModel.sendIntent(HomeIntent.DeleteSession(sessionId))
                    }
                )
            }
            is HomeUiState.Error -> {
                // 에러 상태
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = (uiState as HomeUiState.Error).message,
                        color = Color.Red
                    )
                }
            }
        }
    }
}
