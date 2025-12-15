package com.example.logtalk.ui.groomy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GroomyScreen(onBackClick: () -> Unit) {
    var progressLevel by remember { mutableStateOf(3) } // 1~5 사이 값

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 헤더
        GroomyHeader(onBackClick = onBackClick)

        HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 메인 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
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

            // 구름 이미지와 프로그레스 바
            ProgressBarWithCloud(progressLevel = progressLevel)

            Spacer(modifier = Modifier.height(40.dp))

            // 진행 레벨 조절 버튼들 (테스트용)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..5).forEach { level ->
                    Button(
                        onClick = { progressLevel = level },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (progressLevel == level)
                                Color(0xFF6282E1) else Color.LightGray
                        )
                    ) {
                        Text("$level")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = when (progressLevel) {
                    1 -> "많이 힘드시네요..."
                    2 -> "조금 힘든 상태예요"
                    3 -> "보통이에요"
                    4 -> "기분이 좋네요!"
                    5 -> "최고의 기분이에요!"
                    else -> ""
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6282E1)
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
            .padding(horizontal = 16.dp)
    ) {
        // 뒤로가기 버튼 (왼쪽)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
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

