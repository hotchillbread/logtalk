package com.example.logtalk.ui.chat.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logtalk.ui.chat.composable.ChatContent
import com.example.logtalk.ui.chat.composable.LogTalkAppBar
import com.example.logtalk.ui.chat.composable.MessageInput
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel
import com.example.logtalk.ui.theme.ChatColors

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    onNavigateToSimilarConsultation: () -> Unit,

    viewModel: ChatViewModel
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 커스텀 헤더
        LogTalkAppBar(
            onBackClick = onBackClick,
            onFindSimilarClick = {
                viewModel.findSimilarConsultation()
                onNavigateToSimilarConsultation()
            },
            onReportClick = viewModel::reportChat,
            onDeleteChatClick = {
                viewModel.deleteChat(onChatDeleted = onBackClick)
            }
        )

        // 메인 콘텐츠
        // 메인 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            ChatContent(
                messages = uiState.messages,
                modifier = Modifier.weight(1f)
            )

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = ChatColors.BackgroundPuple
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