package com.example.logtalk.ui.chat.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logtalk.ui.chat.composable.ChatContent
import com.example.logtalk.ui.chat.composable.LogTalkAppBar
import com.example.logtalk.ui.chat.composable.MessageInput
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel


@Composable
fun ChatScreen(
    // TODO: NavController 또는 Navigation 콜백을 위한 파라미터 추가
    onBackClick: () -> Unit,
    // ViewModel은 Hilt/Koin 등의 DI 라이브러리를 통해 주입받는 것이 권장되지만, 여기서는 임시로 viewModel() 사용
    viewModel: ChatViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            LogTalkAppBar(
                onBackClick = onBackClick, // 뒤로 가기 콜백
                onFindSimilarClick = {
                    // TODO: 비슷한 상담 찾기 Navigation 로직 구현 (후순위)
                    viewModel.findSimilarConsultation()
                },
                onReportClick = {
                    // TODO: 신고 기능 로직 구현 (후순위)
                    viewModel.reportChat()
                },
                onDeleteChatClick = {
                    // TODO: 대화 삭제 기능 로직 구현 (
                    viewModel.deleteChat()
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding() // 키보드가 올라올 때 레이아웃이 밀리지 않도록 처리
        ) {
            Divider()

            ChatContent(
                messages = uiState.messages,
                modifier = Modifier.weight(1f)
            )

            // TODO: (추가 기능) uiState.isLoading 상태에 따라 로딩 인디케이터 표시 로직 구현

            MessageInput(
                currentText = uiState.textInput,
                onTextChange = viewModel::updateTextInput, // 텍스트 변경
                onSendClick = viewModel::sendMessage,      // 메시지 전송
                onMicClick = viewModel::sendVoiceMessage   // 마이크 클릭
            )
        }
    }
}