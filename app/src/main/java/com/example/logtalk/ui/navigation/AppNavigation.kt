package com.example.logtalk.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logtalk.MainScreen
import com.example.logtalk.ui.chat.ChatRoute
import com.example.logtalk.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    // 최상위 NavController 생성
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = AppGraph.AUTHENTICATION, // 시작 시 인증 필수
        route = AppGraph.ROOT
    ) {
        // [인증 그래프]
        navigation(
            route = AppGraph.AUTHENTICATION,
            startDestination = AuthScreenRoutes.LOGIN
        ) {
            composable(AuthScreenRoutes.LOGIN) {
                LoginScreen(
                    onGoogleLoginClick = {
                        // 로그인 성공 시 메인으로 이동
                        rootNavController.navigate(AppGraph.MAIN) {
                            popUpTo(AppGraph.AUTHENTICATION) { inclusive = true }
                        }
                    },
                    onSignUpClick = {
                        rootNavController.navigate(AuthScreenRoutes.ONBOARDING)
                    }
                )
            }
            composable(AuthScreenRoutes.ONBOARDING) {
                // TODO: OnboardingScreen() 구현 연결
                Text("온보딩 화면", modifier = Modifier.fillMaxSize())
            }
        }

        // [메인 그래프]
        navigation(
            route = AppGraph.MAIN,
            startDestination = MainScreenRoutes.HOME // "main_screen" 대신 상수 사용 권장
        ) {
            // 1. 메인 홈 화면 (BottomNavigation이 있는 화면)
            composable(MainScreenRoutes.HOME) {
                MainScreen(
                    // 메인 화면에서 리스트 아이템 클릭 시 채팅방으로 이동하는 콜백 예시
                    onChatClick = { titleId ->
                        rootNavController.navigate("${MainScreenRoutes.CHAT}/$titleId")
                    }
                )
            }

            // 2. 채팅 상세 화면
            composable(
                route = "${MainScreenRoutes.CHAT}/{titleId}", // 예: chat/123
                arguments = listOf(
                    navArgument("titleId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                // Hilt가 현재 BackStackEntry(nav arguments 포함)를 기반으로 ViewModel 생성
                ChatRoute(
                    viewModel = hiltViewModel(),
                    onBackClick = { rootNavController.popBackStack() },
                    // 기능 미구현 시 빈 람다 혹은 Toast 메시지 처리
                    onFindSimilarClick = { /* TODO */ },
                    onReportClick = { /* TODO */ },
                    onDeleteChatClick = { /* TODO */ }
                )
            }
        }
    }
}

// (참고) 라우트 상수 정의가 없다면 아래와 같이 추가해서 쓰세요.
object MainScreenRoutes {
    const val HOME = "main_screen"
    const val CHAT = "chat_screen"
}