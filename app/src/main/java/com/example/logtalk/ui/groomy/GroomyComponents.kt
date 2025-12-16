package com.example.logtalk.ui.groomy

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.R
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProgressBarWithCloud(
    chatCount: Int //채팅 횟수 1회당 1% up
) {
    //아래는 디버그용 코드(지금은 필요없어보여서 주석처리함)
    //Log.d("ProgressBarWithCloud", "Rendering with chatCount: $chatCount")

    //채팅 횟수를 퍼센트로 변환 (최대 100%)
    val progressPercentage = chatCount.coerceAtMost(100)
    val progressFloat = progressPercentage / 100f // 0.0 ~ 1.0

    //퍼센트지에 따른 progressLevel
    //0~24%: 1, 25~49%: 2, 50~74%: 3, 75~99%: 4, 100%: 5
    val progressLevel = when {
        progressPercentage in 0..24 -> 1
        progressPercentage in 25..49 -> 2
        progressPercentage in 50..74 -> 3
        progressPercentage in 75..99 -> 4
        else -> 5
    }

    //progressLevel에 따른 구름이 이미지 선택
    val progressDrawable = when (progressLevel) {
        1 -> R.drawable.ic_progress1
        2 -> R.drawable.ic_progress2
        3 -> R.drawable.ic_progress3
        4 -> R.drawable.ic_progress4
        else -> R.drawable.ic_progress5
    }

    //디버깅용 코드
    //Log.d("ProgressBarWithCloud", "Using drawable resource for level: $progressLevel")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        //구르미 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = progressDrawable),
                contentDescription = "Progress Cloud Level $progressLevel",
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )
        }

        //Compose 용 여백 도구
        Spacer(modifier = Modifier.height(30.dp))

        //게이지바 배경
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            //게이지바 진행 상태
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFloat)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFF6282E1),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //퍼센트지 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0%",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "50%",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "100%",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        //현재 퍼센트지 및 채팅 횟수 표시
        Text(
            text = "채팅 횟수: ${chatCount}회 ($progressPercentage%)",
            color = Color(0xFF6282E1),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

