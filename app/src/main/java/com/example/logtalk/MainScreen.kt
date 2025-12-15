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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.logtalk.ui.chat.screen.ChatScreen
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel
import com.example.logtalk.ui.navigation.MainScreenRoutes

import com.example.logtalk.ui.settings.SettingsScreen
import com.example.logtalk.ui.theme.LoginColors
import com.example.logtalk.ui.home.HomeScreen
import com.example.logtalk.ui.navigation.OtherScreenRoutes
import com.example.logtalk.ui.groomy.GroomyScreen
import com.example.logtalk.ui.groomy.GroomyScreenSimple

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
                            currentDestination?.route?.startsWith(screen.route) == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                val targetRoute = if (screen == MainScreenRoutes.Chat) {
                                    "${MainScreenRoutes.Chat.route}/-1"
                                } else {
                                    screen.route
                                }
                                mainNavController.navigate(targetRoute) {
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
                        android.util.Log.d("MainScreen", "Groomy 네비게이션 시작!")
                        mainNavController.navigate(OtherScreenRoutes.GROOMY)
                        android.util.Log.d("MainScreen", "Groomy 네비게이션 명령 완료")
                    },
                    onSessionClick = { sessionId ->
                        // 기존 세션 클릭 시 해당 채팅방으로 이동
                        mainNavController.navigate("${MainScreenRoutes.Chat.route}/$sessionId")
                    },
                    onNewChatClick = {
                        // 새 채팅 시작 시 titleId = -1로 이동 (새 세션 생성)
                        mainNavController.navigate("${MainScreenRoutes.Chat.route}/-1")
                    }
                )
            }

            //chat route
            composable(
                route = "${MainScreenRoutes.Chat.route}/{titleId}", // "chat/{titleId}" 경로
                arguments = listOf(navArgument("titleId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->

                val chatViewModel = hiltViewModel<ChatViewModel>(backStackEntry)

                //ChatScreen 호출 및 모든 콜백 연결
                ChatScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    },
                    // TODO: 유사 상담 화면으로 이동하는 Navigation 로직 연결
                    onNavigateToSimilarConsultation = {
                        // mainNavController.navigate("similar_consultation_route")
                        // 임시로 뒤로가기 대신 로그를 남김
                        println("DEBUG: Navigate to Similar Consultation")
                    },
                    viewModel = chatViewModel
                )
            }
            composable(MainScreenRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    }
                )
            }
            composable(OtherScreenRoutes.GROOMY) {
                android.util.Log.d("MainScreen", "Groomy composable 진입!")
                GroomyScreenSimple(
                    onBackClick = {
                        android.util.Log.d("MainScreen", "Groomy 뒤로가기 클릭")
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    }
                )
            }
        }
    }
}
