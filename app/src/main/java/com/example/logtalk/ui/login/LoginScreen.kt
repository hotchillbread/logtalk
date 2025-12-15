package com.example.logtalk.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.theme.LoginColors
import androidx.compose.ui.graphics.Color

//상단 로고
@Composable
fun LoginLogoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = LoginColors.TextBlack,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Log")
                }
                withStyle(
                    style = SpanStyle(
                        color = LoginColors.TextPurple,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Talk")
                }
            },
            fontSize = 50.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = LoginColors.TextPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("기록")
                }
                withStyle(
                    style = SpanStyle(
                        color = LoginColors.TextBlack,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("에서 시작되는 \n당신의 마음 이야기")
                }
            },
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

//Button용 데이터 프로퍼티
data class LoginButtonConfig(
    val text: String,
    val containerColor: Color,
    val contentColor: Color
)

//로그인, 회원가입 버튼
/**
 * @param config
 * @param onClick
 */
@Composable
fun LoginButton(
    config: LoginButtonConfig,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = config.containerColor,
            contentColor = config.contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = config.text, // 파라미터로 받은 값 사용
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LoginButtonSection(
    onGoogleLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    //구글 계정 객체
    val googleButtonConfig = LoginButtonConfig(
        text = "구글 계정으로 로그인 하기",
        containerColor = LoginColors.Primary,
        contentColor = LoginColors.TextWhite
    )

    //회원가입 객체
    val signUpButtonConfig = LoginButtonConfig(
        text = "새 계정 만들기 (회원가입)",
        containerColor = LoginColors.BackgroundWhite,
        contentColor = LoginColors.TextPurple
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //로그인
        LoginButton(
            config = googleButtonConfig,
            onClick = onGoogleLoginClick
        )
        //회원가입
        LoginButton(
            config = signUpButtonConfig,
            onClick = onSignUpClick
        )
        Text(
            text = "게스트로 빠르게 시작하기",
            color = LoginColors.TextGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp)
        )
    }
}

@Composable
fun LoginScreen(
    onGoogleLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        LoginColors.BackgroundWhite,
                        LoginColors.BackgroundPurple
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .padding(32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween, // 상단-하단 분리
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))
            LoginLogoSection()
            Spacer(modifier = Modifier.weight(1f))
            LoginButtonSection(
                onGoogleLoginClick = onGoogleLoginClick,
                onSignUpClick = onSignUpClick
            )
        }
    }
}

