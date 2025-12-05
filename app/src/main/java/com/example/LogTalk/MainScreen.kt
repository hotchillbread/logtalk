package com.example.logtalk

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.example.logtalk.ui.home.HomeScreen
import com.example.logtalk.ui.navigation.MainScreenRoutes
import com.example.logtalk.ui.settings.SettingsScreen
import com.example.logtalk.ui.theme.LoginColors

// 채팅 목록 화면 (임시로 여기에 정의하거나 별도 파일로 분리 추천)
// 실제로는 DB에서 채팅방 목록(Title 엔티티 리스트)을 가져와 보여주는 화면이어야 합니다.
@Composable
fun ChatListScreen(onChatClick: (Long) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { onChatClick(1L) }) { // 예: ID가 1인 채팅방으로 이동
            Text("1번 채팅방 입장 (테스트)")
        }
    }
}

@Composable
fun MainScreen(
    onChatClick: (Long) -> Unit // AppNavigation에서 전달받은 네비게이션 동작
) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    // 상태바/네비게이션바 색상 설정
    SideEffect {
        window.statusBarColor = Color.White.toArgb()
        window.navigationBarColor = Color.White.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
    }

    val mainNavController = rememberNavController()

    val items = listOf(
        MainScreenRoutes.Home,
        MainScreenRoutes.Chat, // 여기서는 '채팅 목록'을 의미
        MainScreenRoutes.Settings,
    )

    Scaffold(
        bottomBar = {
            Column {
                Divider(
                    color = Color.LightGray.copy(alpha = 0.8f),
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
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
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
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.label,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = screen.label,
                                        fontSize = 14.sp,
                                        color = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha = 0.8f)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White // 선택 시 배경색 제거
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = MainScreenRoutes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. 홈 화면
            composable(MainScreenRoutes.Home.route) {
                // HomeScreen 내부에서 "상담 시작하기" 등의 버튼을 누르면
                // onChatClick을 호출하도록 HomeScreen도 수정이 필요할 수 있습니다.
                HomeScreen()
            }

            // 2. 채팅 목록 탭 (수정됨)
            composable(MainScreenRoutes.Chat.route) {
                // 기존 ChatScreen(상세) 대신 목록 화면을 배치해야 합니다.
                // 여기서 채팅방을 클릭하면 AppNavigation의 chat/{titleId}로 이동합니다.
                ChatListScreen(onChatClick = onChatClick)
            }

            // 3. 설정 화면
            composable(MainScreenRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        if (mainNavController.previousBackStackEntry != null) {
                            mainNavController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}