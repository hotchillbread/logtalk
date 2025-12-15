package com.example.logtalk.ui.relatedChat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logtalk.ui.chat.composable.LogTalkAppBar

@Composable
fun RelatedChatScreen(
    onBackClick: () -> Unit,
    sessionId: Long,
    viewModel: RelatedChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.fetchRelatedChats(sessionId)
    }

    Scaffold(
        topBar = {
            LogTalkAppBar(
                onBackClick = onBackClick,
                onFindSimilarClick = { },
                onReportClick = { },
                onDeleteChatClick = { }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "방금 상담과 비슷한 상담 목록",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            RelatedChatList(chats = uiState.relatedChats)
        }
    }
}

@Composable
fun RelatedChatList(chats: List<RelatedChat>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chats) { chat ->
            RelatedChatItem(chat = chat)
        }
    }
}

@Composable
fun RelatedChatItem(chat: RelatedChat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = chat.date, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = chat.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = chat.summary, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

data class RelatedChat(
    val date: String,
    val title: String,
    val summary: String
)
