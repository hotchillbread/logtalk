package com.example.logtalk


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.logtalk.ui.navigation.MainScreenRoutes
import com.example.logtalk.ui.chat.ChatScreen
import com.example.logtalk.ui.chat.Message
import com.example.logtalk.ui.settings.SettingsScreen
import com.example.logtalk.ui.theme.LoginColors
import com.example.logtalk.ui.home.HomeScreen

@Composable
fun MainScreen() {
    val view = LocalView.current
    val window = (view.context as Activity).window

    SideEffect {
        window.statusBarColor = Color.White.toArgb()
        window.navigationBarColor = Color.White.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
    }
    // nav 생성
    val mainNavController = rememberNavController()

    // 하단 탭 설정
    val items = listOf(
        MainScreenRoutes.Home,
        MainScreenRoutes.Chat,
        MainScreenRoutes.Settings,
    )

    Scaffold(
        bottomBar = {
            Column {

                Divider(
                    color = Color.LightGray.copy(alpha=0.8f),
                    thickness = 0.5.dp
                )

                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.height(72.dp),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                mainNavController.navigate(screen.route) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(top = 0.dp, bottom = 0.dp)
                                ) {
                                    Icon(
                                        screen.icon,
                                        contentDescription = screen.label,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha=0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = screen.label,
                                        fontSize =14.sp,
                                        color = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha=0.8f)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LoginColors.TextPurple,
                                unselectedIconColor = LoginColors.TextGray,
                                selectedTextColor = LoginColors.TextPurple,
                                unselectedTextColor = LoginColors.TextGray,
                                indicatorColor = LoginColors.BackgroundWhite,
                            )
                        )

                    }
                }
            }
        }
    ) { innerPadding ->
        //화면 내부에 nav 호스팅
        NavHost(
            navController = mainNavController,
            startDestination = MainScreenRoutes.Home.route, //시작은 home부터 하도록 설정
            modifier = Modifier.padding(innerPadding)

        ) {

            composable(MainScreenRoutes.Home.route) { HomeScreen() }
            composable(MainScreenRoutes.Chat.route) { ChatScreen(
                messages = dummyMessages,
                sendMessage = {
                    // 실제 메시지 전송 로직 구현 (ViewModel 호출 등)
                    //messages.add(Message(text, true))
                },
                // 뒤로가기 액션
                onBackClick = {
                    mainNavController.popBackStack() // 이 코드가 HomeScreen으로 돌아가게 함
                },
                onFindSimilarClick = { /* TODO: 비슷한 상담 찾기 로직 */ },
                onReportClick = { /* TODO: 신고 로직 */ },
                onDeleteChatClick = { /* TODO: 대화 삭제 로직 */ }
            )
            }
            composable(MainScreenRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    }
                )
            }
        }
    }
}

// 테스트용 더미 데이터
val dummyMessages: List<Message> = listOf(
    Message("안녕하세요! 로그톡 봇입니다.", isUser = false),
    Message("안녕하세요!", true),
    Message(
        "사용자님, 고민에 대해 다시 생각해볼 시간을 드릴게요.",
        false,
        relatedConsultation = "해당 상담내역이 현재 상담과 비슷한 상담을 하고있어요.",
        relatedDate = "2025.11.02",
        directQuestion = "너 생각에는 내가 재수강을 하는게 좋을까?"
    ),
    Message("고민이 많으시군요.", false)
)