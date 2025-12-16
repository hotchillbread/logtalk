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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.R
import android.util.Log

@Composable
fun HomeHeader(onGroomyClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        // 로고 (중앙)
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
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

        // 아이콘 버튼들 (오른쪽)
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = {
                Log.d("HomeHeader", "Groomy 아이콘 클릭!")
                onGroomyClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Groomy",
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(81.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color(0xFF6282E1))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ChatGPT 아이콘 (Linear Gradient: #6282E1 → #FEC3FF)
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
}

@Composable
fun ChatGPTIconWithGradient(modifier: Modifier = Modifier) {
    // Vector Drawable (XML)에 이미 Linear Gradient가 포함되어 있음
    // #6282E1 (0%) → #FEC3FF (100%)
    Image(
        painter = painterResource(id = R.drawable.ic_chatgpt),
        contentDescription = "ChatGPT",
        modifier = modifier
    )
}

@Composable
fun SearchBar(
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(345.dp)
                .height(36.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 검색 아이콘
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 검색 입력 필드
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "상담 기록 검색...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            innerTextField()
                        }
                    )
                }

                // Clear 버튼 (검색어가 있을 때만 표시)
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "검색어 지우기",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SessionList(
    sessions: List<SessionData>,
    onSessionClick: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sessions.size) { index ->
            SessionCard(
                session = sessions[index],
                onClick = { onSessionClick(sessions[index].id) }
            )
        }
    }
}

@Composable
fun SessionCard(
    session: SessionData,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(352.dp)
                .height(78.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFF0F0F0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 텍스트 내용
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = session.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
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
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyState(searchQuery: String = "") {
    val isSearching = searchQuery.isNotEmpty()

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
                imageVector = if (isSearching) Icons.Default.SearchOff else Icons.Default.AddComment,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isSearching) "검색 결과가 없습니다" else "아직 상담 기록이 없습니다",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) "다른 키워드로 검색해보세요" else "새로운 상담을 시작해보세요",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

