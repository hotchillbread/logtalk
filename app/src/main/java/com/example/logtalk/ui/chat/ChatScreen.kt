package com.example.logtalk.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons // Material 2/3 공통
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
// import androidx.compose.material3.* // Material 3 임포트 제거
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.theme.ChatColors // ChatColors는 임시 정의되었다고 가정
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign // 중앙 정렬을 위해 추가
import com.example.logtalk.ui.home.HomeScreen

// 상단 바 (Material 2 TopAppBar로 변경)
@Composable
fun LogTalkAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            // Row를 사용해 title을 강제로 중앙 정렬
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "LogTalk",
                    fontSize = 24.sp,
                    color = ChatColors.BackgroundPuple,
                    textAlign = TextAlign.Center,
                    // 중앙 정렬을 위해 maxLines을 1로 제한하거나 Modifier.weight(1f)를 사용
                    modifier = Modifier.padding(end = 48.dp) // Actions 공간 확보를 위한 임시 패딩
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                    modifier = Modifier.size( 28.dp),
                    tint = ChatColors.TextGray
                )
            }
        },
        actions = {
            IconButton(onClick = { /* 메뉴 액션 */ }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "더 보기",
                    modifier = Modifier.size( 28.dp),
                    tint = ChatColors.TextGray
                )
            }
        },
        backgroundColor = ChatColors.BackgroundWhite,
        elevation = 0.dp
    )
}

//대화
@Composable
fun ChatContent(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
        reverseLayout = false
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

// 메세지 버블
@Composable
fun MessageBubble(message: Message) {

    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = 12.dp,
            bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = 0.dp,
            bottomEnd = 12.dp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = bubbleShape,
            backgroundColor = if (message.isUser) ChatColors.BackgroundPuple else ChatColors.BackgroundGray,
            elevation = 0.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) ChatColors.TextWhite else ChatColors.TextBlack,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}


@Composable
fun MessageInput(
    currentText: String, // 현재 텍스트 값
    onTextChange: (String) -> Unit, // 텍스트 변경 콜백
    onSendClick: () -> Unit, // 전송 버튼 클릭 콜백
    onMicClick: () -> Unit = {} // 마이크 버튼 클릭 콜백 (기본값 설정)
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = currentText,
            onValueChange = onTextChange,
            label = {},
            placeholder = { Text(text = "메시지 전송하기" ) },
            modifier = Modifier.weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = ChatColors.BackgroundInput,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = ChatColors.BackgroundPuple
            ),
            singleLine = true,
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onMicClick) {
            Icon(
                Icons.Filled.Mic,
                contentDescription = "마이크",
                tint = ChatColors.BackgroundPuple,
                modifier = Modifier.size(24.dp)
            )
        }

        // 전송 아이콘 버튼
        IconButton(onClick = onSendClick, enabled = currentText.isNotBlank()) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "전송",
                tint = ChatColors.BackgroundPuple,
            )
        }
    }
}

//임시 데이터 나중에 state에서 관리하는걸로 변경
data class Message(val text: String, val isUser: Boolean)


// 전송, 마이크 interaction 추가필요
// 상단 케밥버튼 인터페이스 추가
// 뒤로가기 누르면 homeScreen()이동 해야함 <- 다른 사람이 하면 추가
// 봇 메세지 누르면 신고하기 나오게 하기(화면만)
@Composable
fun ChatScreen(
    messages: List<Message>,
    sendMessage: (String) -> Unit,
    onBackClick: () -> Unit,
    //sendVoice, sendReport, findSimilarChat 등 추가
) {
    var textInput by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            LogTalkAppBar(onBackClick = onBackClick)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ChatContent(
                messages = messages,
                modifier = Modifier.weight(1f)
            )
            MessageInput(currentText = textInput,
                onTextChange = { textInput = it },
                onSendClick = {
                    if (textInput.isNotBlank()) {
                        sendMessage(textInput)
                        textInput = "" // 메시지 전송 후 입력 필드 초기화
                    }
                })

        }

    }
}