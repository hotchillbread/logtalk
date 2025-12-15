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

/**
 * 간단한 테스트용 Groomy 화면
 * 문제를 진단하기 위해 복잡한 ViewModel과 리소스 로딩을 제거
 */
@Composable
fun GroomyScreenSimple(
    onBackClick: () -> Unit
) {
    Log.d("GroomyScreenSimple", "GroomyScreenSimple composed")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            // 뒤로가기 버튼
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

            // 제목
            Text(
                text = "Groomy (Simple)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6282E1),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        // 메인 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "✅ Groomy 화면이 정상 작동합니다!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6282E1)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "이 화면이 보인다면\n네비게이션은 정상입니다.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6282E1)
                )
            ) {
                Text("홈으로 돌아가기")
            }
        }
    }
}

