package com.example.logtalk.ui.navigation

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

//전체 흐름 관리를 위한 싱글톤 객체
object AppGraph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
}

//메인 화면 라우트
//sealed class로 캡슐화 가능, 지정된 키워드 외 상속 불가능
sealed class MainScreenRoutes(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    //icon 지정해야됨
    object Home : MainScreenRoutes("home", "홈", Icons.Default.Home)
    object Chat : MainScreenRoutes("chat", "채팅", Icons.Default.ChatBubble)
    object Settings : MainScreenRoutes("settings", "설정", Icons.Default.Settings)

    companion object {
        const val ChatDetail = "chat/{titleId}"
    }
}

//인증 라우트
object  AuthScreenRoutes {
    const val LOGIN = "login"
    const val ONBOARDING = "onboarding"
}

//기타 라우트
object OtherScreenRoutes {
    const val GROOMY = "groomy"
    const val INSIGHT = "insight"
    const val RELATED_CHAT = "related_chat/{consultationId}"
}
