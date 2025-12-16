package com.example.logtalk.ui.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.* // Material 2 사용
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text // Material 3 Text 사용 (선호도에 따라)
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.theme.ChatColors // ChatColors는 임시 정의되었다고 가정
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle


@Composable
fun LogTalkAppBar(onBackClick: () -> Unit,
                  onFindSimilarClick: () -> Unit,
                  onReportClick: () -> Unit,
                  onDeleteChatClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val logStyle = SpanStyle(color = Color.Black)
    val talkStyle = SpanStyle(color = Color(0xFF6282E1))

    val annotatedString = buildAnnotatedString {
        withStyle(style = logStyle) {
            append("Log")
        }
        withStyle(style = talkStyle) {
            append("Talk")
        }
    }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.height(56.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = annotatedString,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                    modifier = Modifier.size(28.dp),
                    tint = ChatColors.TextGray
                )
            }
        },
        actions = {
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
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
                    //여기 누르면 아예 새로운 페이지로 가도록 설정
                    DropdownMenuItem(onClick = { expanded = false; onFindSimilarClick() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "비슷한 상담 찾기", tint = ChatColors.TextGray)
                            Spacer(Modifier.width(8.dp))
                            Text("비슷한 상담 찾기")
                        }
                    }
                    DropdownMenuItem(onClick = { expanded = false; onReportClick() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Flag, contentDescription = "신고", tint = ChatColors.TextGray)
                            Spacer(Modifier.width(8.dp))
                            Text("신고")
                        }
                    }
                    DropdownMenuItem(onClick = { expanded = false; onDeleteChatClick() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "대화 삭제", tint = ChatColors.TextRed)
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

@Composable
fun ChatContent(messages: List<Message>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    // 메시지 목록이 업데이트될 때마다 가장 마지막 메시지로 스크롤
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
        state = listState,
        reverseLayout = false // 가장 아래로 스크롤하는 방식이므로 reverseLayout = false 유지
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    //여기는 단일 컴포넌트 블럭이라 문제없음

    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 12.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        // 채팅 버블 (Card)
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
    currentText: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = currentText,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .focusRequester(focusRequester),
            textStyle = TextStyle(fontSize = 16.sp, color = LocalContentColor.current),
            singleLine = true,
            cursorBrush = SolidColor(ChatColors.BackgroundPuple),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),

            keyboardActions = KeyboardActions(
                onSend = {
                    if (currentText.isNotBlank()) {
                        onSendClick()
                    }
                }
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = ChatColors.BackgroundInput,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (currentText.isEmpty()) {
                        Text(
                            text = "고민을 입력해주세요",
                            fontSize = 16.sp,
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        // TODO: (추가 기능) 마이크 클릭 시 음성 녹음 UI 표시 로직 구현
        IconButton(onClick = onMicClick) {
            Icon(
                Icons.Filled.Mic,
                contentDescription = "마이크",
                tint = ChatColors.BackgroundPuple,
                modifier = Modifier.size(32.dp)
            )
        }

        // 전송 버튼->텍스트가 있을 때만 활성화
        IconButton(onClick = onSendClick, enabled = currentText.isNotBlank()) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "전송",
                tint = if (currentText.isNotBlank()) ChatColors.BackgroundPuple else ChatColors.TextGray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}