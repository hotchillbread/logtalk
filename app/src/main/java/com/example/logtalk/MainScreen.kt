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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.screen.ChatScreen
import com.example.logtalk.ui.navigation.MainScreenRoutes
import com.example.logtalk.ui.navigation.OtherScreenRoutes

import com.example.logtalk.ui.settings.SettingsScreen
import com.example.logtalk.ui.theme.LoginColors
import com.example.logtalk.ui.home.HomeScreen
import com.example.logtalk.ui.groomy.GroomyScreen

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

            composable(MainScreenRoutes.Home.route) {
                HomeScreen(
                    onGroomyClick = {
                        mainNavController.navigate(OtherScreenRoutes.GROOMY)
                    }
                )
            }
            composable(MainScreenRoutes.Chat.route) { ChatScreen(
                onBackClick = {
                    mainNavController.popBackStack() // 이 코드가 HomeScreen으로 돌아가게 함
                },
                viewModel = viewModel()
            )
            }
            //여기서 viewmodel 라우팅 해줘야돼요!!!!!! 꼭 하자 OK?
            composable(MainScreenRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    }
                )
            }
            composable(OtherScreenRoutes.GROOMY) {
                GroomyScreen(
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
    // 1. 봇 메시지 (ID: 1)
    Message(
        id = 1L,
        text = "안녕하세요! 로그톡 봇입니다. 어떤 고민이 있으신가요?",
        isUser = false
    ),

    // 2. 사용자 메시지 (ID: 2)
    Message(
        id = 2L,
        text = "요즘 진로 문제 때문에 고민이 많아요. 전공을 바꿔야 할까요?",
        isUser = true
    ),

    // 3. 봇 메시지 - 관련 상담 제안 포함 (ID: 3)
    // relatedConsultation, relatedDate, directQuestion 필드를 활용한 예시
    Message(
        id = 3L,
        text = "사용자님의 고민과 비슷한 내용을 이전에 상담하셨습니다. 관련 상담 내용을 참고해보시는 건 어떨까요?",
        isUser = false,
        relatedConsultation = "지난 상담에서는 이직을 고민하셨고, 결국 성공적인 결정을 내리셨습니다.",
        relatedDate = "2025.11.02",
        directQuestion = "지금 느끼는 불안감의 핵심 원인은 무엇이라고 생각하시나요?"
    ),

    // 4. 사용자 메시지 (ID: 4)
    Message(
        id = 4L,
        text = "네, 그때와 비슷한 복잡한 감정인 것 같아요.",
        isUser = true
    ),

    // 5. 봇 메시지 (ID: 5)
    Message(
        id = 5L,
        text = "이해합니다. 그럼 저희가 함께 이 문제를 깊이 있게 탐색해 봅시다.",
        isUser = false
    )
)