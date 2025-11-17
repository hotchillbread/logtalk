package com.example.logtalk.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons // Material 2/3 공통
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign // 중앙 정렬을 위해 추가
import com.example.logtalk.ui.home.HomeScreen

@Composable
fun LogTalkAppBar(onBackClick: () -> Unit, //뒤로가기 (홈으로)
                  onFindSimilarClick: () -> Unit, // 비슷한 상담 찾기
                  onReportClick: () -> Unit,      // 신고
                  onDeleteChatClick: () -> Unit   // 채팅 삭제
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "LogTalk",
                    fontSize = 24.sp,
                    color = ChatColors.BackgroundPuple,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 48.dp)
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
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = true }) { //클릭시 true로 변경
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "더 보기",
                        modifier = Modifier.size(28.dp),
                        tint = ChatColors.TextBlack
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(onClick = {
                        expanded = false
                        onFindSimilarClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "비슷한 상담 찾기",
                                tint = ChatColors.TextBlack
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("비슷한 상담 찾기")
                        }
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onReportClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "신고",
                                tint = ChatColors.TextBlack
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("신고")
                        }
                    }

                    DropdownMenuItem(onClick = {
                        expanded = false
                        onDeleteChatClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "대화 삭제",
                                tint = ChatColors.TextRed
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("대화 삭제", color = ChatColors.TextRed)
                        }
                    }
                }
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
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
        if (message.relatedConsultation != null) {
            RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = 0.dp,
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
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        //채팅 버블 (Card)
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
        // 리다이렉션이 있는 경우
        if (!message.isUser && message.relatedConsultation != null) {
            RelatedConsultationBlock(message)
        }
    }
}

@Composable
fun RelatedConsultationBlock(message: Message) {
    val blockShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

    Card(
        shape = blockShape,
        backgroundColor = ChatColors.BackgroundGray,
        elevation = 0.dp,
        modifier = Modifier
            .widthIn(max = 300.dp)
            .padding(top = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            if (message.relatedDate != null && message.directQuestion != null) {
                Divider(color = ChatColors.TextGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "${message.relatedDate}",
                    color = ChatColors.TextGray, // 날짜 색상
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = message.directQuestion,
                    color = ChatColors.BackgroundPuple,
                    fontSize = 15.sp,
                    // 사용자 정의 클릭 로직
                    //`ChatScreen`에서 `onQuestionClick: (String) -> Unit` 콜백설정하셈!
                    modifier = Modifier
                        .padding(top = 2.dp)
                    // .clickable { onQuestionClick(message.directQuestion) } // 클릭 리스너 추가해야함 ㅠ
                )
            }
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

//임시데이터
data class Message(
    val text: String,
    val isUser: Boolean,
    // 하단에 표시할 '관련 상담 내용' 필드
    val relatedConsultation: String? = null,
    // 관련 상담이 있을 경우의 날짜
    val relatedDate: String? = null,
    // 관련 상담 아래에 표시할 다이렉트 질문
    val directQuestion: String? = null
)


// 전송, 마이크 interaction 추가필요
// 상단 케밥버튼 인터페이스 추가
// 뒤로가기 누르면 homeScreen()이동 해야함 <- 다른 사람이 하면 추가
// 봇 메세지 누르면 신고하기 나오게 하기(화면만)
@Composable
fun ChatScreen(
    messages: List<Message>,
    sendMessage: (String) -> Unit,
    onBackClick: () -> Unit,
    onFindSimilarClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    //sendVoice, sendReport, findSimilarChat 등 추가
) {
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            LogTalkAppBar(onBackClick = onBackClick,
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
            Divider()
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