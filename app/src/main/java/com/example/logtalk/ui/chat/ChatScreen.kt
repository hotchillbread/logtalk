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
import androidx.compose.ui.text.style.TextAlign // 중앙 정렬을 위해 추가


//임시 데이터 나중에 state에서 관리하는걸로 변경
data class Message(val text: String, val isUser: Boolean)

@Composable
fun ChatScreen() {
    val messages = remember {
        mutableStateListOf(
            Message("오늘의 기분은 어떠신가요?\n저에게 알려주세요", isUser = false),
            Message("흡흡 안드로이드 스튜디오만 돌리면\n컴퓨터가 죽으려고 해 ㅠㅠ", isUser = true)
        )
    }

    // ★ Material 2 Scaffold 사용
    Scaffold(
        topBar = { LogTalkAppBar() },
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
            MessageInputField()
        }
    }
}

// 상단 바 (Material 2 TopAppBar로 변경)
@Composable
fun LogTalkAppBar() {
    // ★ Material 2 TopAppBar 사용 (안정화됨)
    TopAppBar(
        title = {
            // Row를 사용해 title을 강제로 중앙 정렬
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "LogTalk",
                    fontSize = 18.sp,
                    color = ChatColors.TextBlack,
                    textAlign = TextAlign.Center,
                    // 중앙 정렬을 위해 maxLines을 1로 제한하거나 Modifier.weight(1f)를 사용
                    modifier = Modifier.padding(end = 48.dp) // Actions 공간 확보를 위한 임시 패딩
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*홈으로 보내야함 */}) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                    tint = ChatColors.TextBlack
                )
            }
        },
        actions = {
            IconButton(onClick = { /* 메뉴 액션 */ }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "더 보기",
                    tint = ChatColors.TextBlack
                )
            }
        },
        backgroundColor = ChatColors.BackgroundWhite, // Material 2 스타일
        elevation = 0.dp // 그림자 제거
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        // ★ Material 2 Card 사용 (elevation = 0.dp로 그림자 제거)
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = if (message.isUser) ChatColors.BackgroundPuple else ChatColors.BackgroundGray,
            elevation = 0.dp, // Material 2에서 그림자 제거
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
fun MessageInputField() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState },
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

        IconButton(onClick = { /* 마이크 액션 */ }) {
            Icon(
                Icons.Filled.Mic,
                contentDescription = "마이크",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        // 전송 아이콘 버튼
        IconButton(onClick = { /* 전송 액션 */ }) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "전송",
                tint = Color.Gray
            )
        }
    }
}