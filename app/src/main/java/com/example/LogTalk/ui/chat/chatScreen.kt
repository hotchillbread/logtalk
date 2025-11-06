// 📍 위치: com.example.logtalk.ui.chat.ChatScreen.kt

package com.example.logtalk.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * 챗봇 화면의 메인 Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel()
) {
    // ViewModel의 uiState를 구독. state가 변경되면 이 Composable이 재구성됨.
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ChatTopBar(
                onBackClick = { viewModel.onBackClick() },
                onKebabClick = { viewModel.onKebabMenuClick() }
            )
        },
        bottomBar = {
            ChatInputBar(
                text = uiState.inputText,
                onTextChanged = { viewModel.onInputTextChanged(it) },
                onSendClick = { viewModel.sendMessage() },
                onVoiceRecordClick = { viewModel.onVoiceRecordClick() },
                isRecording = uiState.isRecording
            )
        }
    ) { innerPadding ->
        ChatMessageList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            messages = uiState.messages,
            isLoading = uiState.isLoading
        )
    }
}

// --- 하위 컴포넌트들 ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    onBackClick: () -> Unit,
    onKebabClick: () -> Unit
) {
    TopAppBar(
        title = { Text("LogTalk AI 상담") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로 가기")
            }
        },
        actions = {
            IconButton(onClick = onKebabClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기")
            }
        }
    )
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onVoiceRecordClick: () -> Unit,
    isRecording: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 음성 녹음 버튼
        IconButton(onClick = onVoiceRecordClick) {
            Icon(
                Icons.Default.Mic,
                contentDescription = "음성 녹음",
                tint = if (isRecording) Color.Red else LocalContentColor.current
            )
        }

        // 텍스트 입력창
        TextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text("메시지를 입력하세요...") }
        )

        // 전송 버튼
        IconButton(onClick = onSendClick) {
            Icon(Icons.Default.Send, contentDescription = "전송")
        }
    }
}

@Composable
fun ChatMessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    isLoading: Boolean
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        reverseLayout = true // 채팅은 아래부터 쌓여야 하므로
    ) {
        // 로딩 스피너 (isLoading이 true일 때만 표시)
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        // 메시지 목록
        items(
            items = messages.reversed(), // reverseLayout=true이므로 목록도 뒤집음
            key = { it.id } // 각 아이템의 고유 ID
        ) { message ->
            if (message.isFromUser) {
                UserMessageItem(message = message)
            } else {
                AiMessageItem(message = message)
            }
        }
    }
}

@Composable
fun UserMessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End // 오른쪽 정렬
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun AiMessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start // 왼쪽 정렬
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}