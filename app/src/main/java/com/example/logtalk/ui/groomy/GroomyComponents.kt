package com.example.logtalk.ui.groomy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.R

@Composable
fun ProgressBarWithCloud(progressLevel: Int) {
    // progressLevel: 1~5 (ic_progress1 ~ ic_progress5 사용)
    val clampedLevel = progressLevel.coerceIn(1, 5)
    val progressPercentage = (clampedLevel - 1) / 4f // 0.0 ~ 1.0

    // progress 이미지 리소스 ID
    val progressDrawable = when (clampedLevel) {
        1 -> R.drawable.ic_progress1
        2 -> R.drawable.ic_progress2
        3 -> R.drawable.ic_progress3
        4 -> R.drawable.ic_progress4
        else -> R.drawable.ic_progress5
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // 구름 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = progressDrawable),
                contentDescription = "Progress Cloud",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 게이지바 배경
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            // 게이지바 진행 상태
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressPercentage)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFF6282E1),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 퍼센트 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material3.Text(
                text = "0%",
                color = Color.Gray,
                fontSize = 14.sp
            )
            androidx.compose.material3.Text(
                text = "50%",
                color = Color.Gray,
                fontSize = 14.sp
            )
            androidx.compose.material3.Text(
                text = "100%",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

