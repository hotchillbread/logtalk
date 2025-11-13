package com.example.logtalk.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.R

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 로고
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                color = Color(0xFF6366F1)
            )
        }

        // 아이콘 버튼들
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = { /* TODO: 노트 */ }) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "노트",
                    tint = Color.Gray
                )
            }
            IconButton(onClick = { /* TODO: 설정 */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "설정",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun NewChatBanner(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, Color(0xFF6282E1))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ChatGPT 아이콘 (Linear Gradient: #6282E1 → #FEC3FF)
            // 배경 투명, 아이콘 선만 그라데이션
            ChatGPTIconWithGradient(
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 텍스트
            Column {
                Text(
                    text = "상담을 시작해볼까요?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6282E1),
                    letterSpacing = 0.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "› 눌러서 새로운 대화 시작하기",
                    fontSize = 13.sp,
                    color = Color(0xFFB8B8B8),
                    letterSpacing = 0.sp
                )
            }
        }
    }
}

@Composable
fun ChatGPTIconWithGradient(modifier: Modifier = Modifier) {
    // 투명 배경 PNG 아이콘의 선(알파 영역)에만 Linear Gradient 적용
    // #6282E1 (0%) → #FEC3FF (100%)
    Image(
        painter = painterResource(id = R.drawable.ic_chatgpt),
        contentDescription = "ChatGPT",
        modifier = modifier
            .drawWithContent {
                drawContent()
                // 아이콘의 알파가 있는 부분(선)에만 그라데이션 색상 적용
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6282E1),  // 0% - 파란색
                            Color(0xFFFEC3FF)   // 100% - 핑크색
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height)
                    ),
                    blendMode = BlendMode.SrcAtop  // 원본 알파 채널을 유지하면서 색상만 적용
                )
            }
    )
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* TODO: 검색 */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = "상담 기록 검색...",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6366F1),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        ),
        singleLine = true
    )
}

@Composable
fun SessionList(sessions: List<SessionData>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(sessions.size) { index ->
            SessionCard(session = sessions[index])
            if (index < sessions.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    color = Color(0xFFF0F0F0)
                )
            }
        }
    }
}

@Composable
fun SessionCard(session: SessionData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: 세션 열기 */ }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 아이콘
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 텍스트 내용
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top)
        ) {
            Text(
                text = session.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = session.lastMessage,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 시간
        Text(
            text = session.timeAgo,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 아이콘
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(60.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddComment,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "아직 상담 기록이 없습니다",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "새로운 상담을 시작해보세요",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

