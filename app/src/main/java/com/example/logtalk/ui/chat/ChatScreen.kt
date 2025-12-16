package com.example.logtalk.ui.chat.screen
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.Snackbar
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logtalk.ui.chat.composable.ChatContent
import com.example.logtalk.ui.chat.composable.LogTalkAppBar
import com.example.logtalk.ui.chat.composable.MessageInput
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel
import com.example.logtalk.ui.theme.ChatColors // 가정된 색상

@Composable
public fun ChatScreen(
    onBackClick: () -> Unit,
    onNavigateToSimilarConsultation: () -> Unit,

    viewModel: ChatViewModel
) {
    val uiState = viewModel.uiState


    Scaffold(
        topBar = {
            LogTalkAppBar(
                onBackClick = onBackClick, // 뒤로 가기
                onFindSimilarClick = {
                    // 뷰모델 상태 업데이트 후 Navigation 실행
                    viewModel.findSimilarConsultation()
                    onNavigateToSimilarConsultation() // 실제 화면 이동 실행
                },
                onReportClick = viewModel::reportChat, // 신고 기능
                onDeleteChatClick = {
                    viewModel.deleteChat(onChatDeleted = onBackClick)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            Divider()

            ChatContent(
                messages = uiState.messages,
                modifier = Modifier.weight(1f)
            )

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = ChatColors.BackgroundPuple // 가정된 색상
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            MessageInput(
                currentText = uiState.textInput ?: "",
                onTextChange = viewModel::updateTextInput,
                onSendClick = viewModel::sendMessage,
                onMicClick = viewModel::sendVoiceMessage
            )
        }
    }
}