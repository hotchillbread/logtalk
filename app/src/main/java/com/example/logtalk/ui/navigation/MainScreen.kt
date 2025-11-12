package com.example.logtalk.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat // 예시 아이콘
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings // 예시 아이콘
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.logtalk.ui.navigation.MainScreenRoutes
import com.example.logtalk.ui.chat.ChatScreen
import com.example.logtalk.ui.theme.LoginColors

// 임시 화면
@Composable fun HomeScreen() { Text("홈 화면") }

@Composable fun SettingsScreen() { Text("설정 화면") }


@Composable
fun MainScreen() {
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
            NavigationBar(
                modifier = Modifier.height(48.dp)
            ) {
                val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = isSelected,
                        onClick = {
                            mainNavController.navigate(screen.route) {
                                // 맨위 팝업 시키기
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // 같은 목적지 재실행 방지
                                launchSingleTop = true
                                // 상태 복원
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LoginColors.TextPurple,
                            unselectedIconColor = LoginColors.TextBlack,
                        )
                    )
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
            composable(MainScreenRoutes.Chat.route) { ChatScreen() }
            composable(MainScreenRoutes.Settings.route) { SettingsScreen() }
        }
    }
}
