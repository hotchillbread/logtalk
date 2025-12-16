package com.example.logtalk.ui.chat.screen // 적절한 패키지 경로

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.chat.composable.LogTalkAppBar
import com.example.logtalk.ui.chat.viewmodel.RelatedChatViewModel
import com.example.logtalk.ui.chat.viewmodel.RelatedConsultationItem
import com.example.logtalk.ui.theme.ChatColors

@Composable
fun RelatedChatScreen(
    onBackClick: () -> Unit,
    viewModel: RelatedChatViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {

            LogTalkAppBar(
                onBackClick = onBackClick,
                onFindSimilarClick = { /* RelatedChatScreen에서는 무시 */ },
                onReportClick = { /* RelatedChatScreen에서는 무시 */ },
                onDeleteChatClick = { /* RelatedChatScreen에서는 무시 */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // "방금 상담과 비슷한 상담 목록" 타이틀 영역 (이미지와 동일한 디자인)
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp)) {
                Text(
                    text = "방금 상담과 비슷한 상담 목록",
                    fontSize = 16.sp,
                    color = Color.Gray // 제목 위 회색 텍스트
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "비슷한 상담 목록",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChatColors.TextBlack
                )
            }
            Divider()


            // 목록 컨텐츠
            when {
                uiState.isLoading -> LoadingState()
                uiState.errorMessage != null -> ErrorState(uiState.errorMessage!!)
                uiState.relatedChats.isEmpty() -> EmptyState()
                else -> RelatedConsultationList(
                    items = uiState.relatedChats,
                    onItemClick = viewModel::onConsultationItemClick
                )
            }
        }
    }
}
@Composable
fun RelatedConsultationList(
    items: List<RelatedConsultationItem>,
    onItemClick: (RelatedConsultationItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items) { item ->
            RelatedConsultationItemView(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
fun RelatedConsultationItemView(
    item: RelatedConsultationItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = item.date,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            color = ChatColors.TextBlack
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.summary,
            fontSize = 14.sp,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = ChatColors.BackgroundPuple)
    }
}

@Composable
fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "에러 발생: $message", color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "유사한 상담 기록이 없습니다.", color = Color.Gray, modifier = Modifier.padding(16.dp))
    }
}