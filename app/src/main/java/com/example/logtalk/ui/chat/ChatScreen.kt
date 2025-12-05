package com.example.logtalk.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logtalk.ui.theme.ChatColors

// --- [UI Model] ---
// 이 데이터 클래스는 두 파일 모두에서 사용되므로 같은 패키지에 있어야 합니다.
data class Message(
    val text: String,
    val isUser: Boolean,
    val relatedConsultation: String? = null,
    val relatedDate: String? = null,
    val directQuestion: String? = null
)

// --- [Entry Point] ---
@Composable
fun ChatRoute(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit,
    onFindSimilarClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onDeleteChatClick: () -> Unit = {}
) {
    // ViewModel의 StateFlow를 구독하여 최신 상태를 가져옵니다.
    val uiState by viewModel.uiState.collectAsState()

    ChatScreen(
        uiState = uiState,
        onSendMessage = viewModel::sendMessage, // 전송 로직(람다) 전달
        onBackClick = onBackClick,
        onFindSimilarClick = onFindSimilarClick,
        onReportClick = onReportClick,
        onDeleteChatClick = onDeleteChatClick,
        onErrorDismiss = { /* 에러 처리 로직 */ }
    )
}

// --- [Screen Layout] ---
@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onSendMessage: (String) -> Unit,
    onBackClick: () -> Unit,
    onFindSimilarClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    onErrorDismiss: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()

    // 에러 스낵바 처리
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            scaffoldState.snackbarHostState.showSnackbar(message)
            onErrorDismiss()
        }
    }

    // 메시지 추가 시 스크롤 하단 이동
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            LogTalkAppBar(
                onBackClick = onBackClick,
                onFindSimilarClick = onFindSimilarClick,
                onReportClick = onReportClick,
                onDeleteChatClick = onDeleteChatClick
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(color = ChatColors.BackgroundGray, thickness = 1.dp)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // 컴포넌트 파일에 있는 ChatContent 호출
                ChatContent(
                    messages = uiState.messages,
                    listState = listState,
                    modifier = Modifier.fillMaxSize()
                )

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ChatColors.BackgroundPuple
                    )
                }
            }

            // 컴포넌트 파일에 있는 MessageInput 호출
            MessageInput(
                currentText = textInput,
                onTextChange = { textInput = it },
                onSendClick = {
                    if (textInput.isNotBlank()) {
                        onSendMessage(textInput) // 뷰모델의 sendMessage 실행
                        textInput = ""
                    }
                },
                isLoading = uiState.isLoading
            )
        }
    }
}