package com.example.logtalk.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.R

@Composable
fun LoginScreen(
    onGoogleLoginClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginColors.Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            //메인 로고
            Text(
                text = "LogTalk",
                color = LoginColors.TextBlack,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )

            //서브 텍스트?
            Text(
                text = "기록에서 시작되는\n" +
                        "당신의 마음 이야기",
                color = LoginColors.TextGray,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            //로그인 버튼 (구글 로그인 연결해야됨)
            Button(
                //firebase api 연결필요
                onClick = onGoogleLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginColors.Primary,
                    contentColor = LoginColors.TextWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                // 구글 로고 아이콘을 나중에 추가 가능
                Text("Continue with Google", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Button(
                //firebase api 연결필요
                onClick = onGoogleLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginColors.Background,
                    contentColor = LoginColors.TextBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                // 구글 로고 아이콘을 나중에 추가 가능
                Text("Continue with Google", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1F1F1F)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}