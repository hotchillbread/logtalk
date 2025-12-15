package com.example.logtalk.ui.groomy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GroomyScreen(
    onBackClick: () -> Unit,
    viewModel: GroomyViewModel = hiltViewModel()
) {
    // State를 안전하게 수집
    val totalMessageCount by viewModel.totalMessageCount.collectAsState()
    val progressLevel by viewModel.progressLevel.collectAsState()
    val emotionMessage by viewModel.emotionMessage.collectAsState()

    // 렌더링 로그
    LaunchedEffect(Unit) {
        Log.d("GroomyScreen", "GroomyScreen composed")
    }

    LaunchedEffect(totalMessageCount, progressLevel, emotionMessage) {
        Log.d("GroomyScreen", "State updated - count: $totalMessageCount, level: $progressLevel, message: $emotionMessage")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // 헤더
        GroomyHeader(onBackClick = onBackClick)

        HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        // 메인 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Groomy",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6282E1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "나의 감정 상태",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(60.dp))

            // 구름 이미지와 프로그레스 바 (채팅 횟수 기반)
            ProgressBarWithCloud(chatCount = totalMessageCount)

            Spacer(modifier = Modifier.height(40.dp))

            // 감정 상태 메시지
            Text(
                text = emotionMessage,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6282E1)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 프로그레스 레벨 표시 (디버그용)
            Text(
                text = "레벨: $progressLevel / 5",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 디버그 정보
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "총 메시지 수: $totalMessageCount",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun GroomyHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // 뒤로가기 버튼 (왼쪽)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.Gray
            )
        }

        // 제목 (중앙)
        Text(
            text = "Groomy",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6282E1),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

