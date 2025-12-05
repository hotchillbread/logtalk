package com.example.logtalk.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.theme.ChatColors

// 상단 앱바
@Composable
fun LogTalkAppBar(
    onBackClick: () -> Unit,
    onFindSimilarClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteChatClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Log",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Talk",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6282E1)
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
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onFindSimilarClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, "비슷한 상담 찾기", tint = ChatColors.TextGray)
                            Spacer(Modifier.width(8.dp))
                            Text("비슷한 상담 찾기")
                        }
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onReportClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, "신고", tint = ChatColors.TextGray)
                            Spacer(Modifier.width(8.dp))
                            Text("신고")
                        }
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onDeleteChatClick()
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Delete, "대화 삭제", tint = ChatColors.TextRed)
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

// 채팅 리스트 영역
@Composable
fun ChatContent(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(
        state = listState,
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

// 개별 메시지 버블
@Composable
fun MessageBubble(message: Message) {
    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)
    } else {
        if (message.relatedConsultation != null) {
            RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)
        } else {
            RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
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
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            )
        }

        if (!message.isUser && message.relatedConsultation != null) {
            RelatedConsultationBlock(message)
        }
    }
}

// 관련 상담 블록 (AI 답변 하단)
@Composable
fun RelatedConsultationBlock(message: Message) {
    val blockShape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)

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
                Divider(
                    color = ChatColors.TextGray,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "${message.relatedDate}",
                    color = ChatColors.TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = message.directQuestion,
                    color = ChatColors.BackgroundPuple,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// 하단 메시지 입력창
@Composable
fun MessageInput(
    currentText: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                .height(56.dp)
                .focusRequester(focusRequester),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            singleLine = true,
            cursorBrush = SolidColor(ChatColors.BackgroundPuple),
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
                            text = "메시지 전송하기",
                            fontSize = 16.sp,
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    }
                    innerTextField()
                }
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onMicClick, enabled = !isLoading) {
            Icon(
                Icons.Filled.Mic,
                contentDescription = "마이크",
                tint = if (isLoading) Color.Gray else ChatColors.BackgroundPuple,
                modifier = Modifier.size(32.dp)
            )
        }

        IconButton(
            onClick = onSendClick,
            enabled = currentText.isNotBlank() && !isLoading
        ) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "전송",
                tint = if (currentText.isNotBlank() && !isLoading) ChatColors.BackgroundPuple else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}