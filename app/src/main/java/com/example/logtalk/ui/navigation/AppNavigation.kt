package com.example.logtalk.ui.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.logtalk.ui.login.LoginScreen
import com.example.logtalk.ui.navigation.MainScreen // 방금 만든 MainScreen 임포트

@Composable
fun AppNavigation() {
    //최상위 NavController 생성
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = AppGraph.AUTHENTICATION, //시작시 인증 필수
        route = AppGraph.ROOT
    ) {
        navigation(
            route = AppGraph.AUTHENTICATION,
            startDestination = AuthScreenRoutes.LOGIN
        ) {
            composable(AuthScreenRoutes.LOGIN) {
                // Login
                LoginScreen(
                    onGoogleLoginClick = {
                        // 로그인 성공으로 임시 저장(나중에 로직 추가)
                        rootNavController.navigate(AppGraph.MAIN) {
                            // 인증 제거
                            popUpTo(AppGraph.AUTHENTICATION) {
                                inclusive = true
                            }
                        }
                    },
                    //회원가입 시
                    onSignUpClick = {
                        rootNavController.navigate(AuthScreenRoutes.ONBOARDING)
                        //하단에 온보딩으로 보내는 로직 작성
                    }
                )
            }
            composable(AuthScreenRoutes.ONBOARDING) {
                //OnboardingScreen()을 호출 필요
                Text("온보딩 화면", modifier = Modifier.fillMaxSize())
            }
        }

        // 메인
        navigation(
            route = AppGraph.MAIN,
            startDestination =  "main_screen"
        ) {
            composable("main_screen") {
                MainScreen()
            }
            // 상세 라우팅 해야함
        }
    }
}