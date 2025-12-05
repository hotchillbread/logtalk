package com.example.logtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.logtalk.ui.login.LoginScreen
import com.example.logtalk.ui.theme.LogTalkTheme
import com.example.logtalk.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.data.AppDatabase

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. EnvManager 초기화
        // (AppModule에서 provideApiKey를 하려면 여기서 초기화가 먼저 되어야 안전합니다.)
        try {
            EnvManager.initialize { isSuccessful ->
                if (isSuccessful) {
                    Logger.d("API Key 준비됨.")
                } else {
                    Logger.e("초기화 실패.")
                    // 여기서 종료하거나 에러 화면 처리가 필요할 수 있음
                }
            }
        } catch (e: Exception) {
            Logger.e("환경설정 로드 중 예외 발생: ${e.message}")
        }

        // 2. 시스템 UI 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        Logger.d("app start")
        enableEdgeToEdge()

        // 3. UI 시작 (DB 초기화 코드는 삭제됨 -> Hilt가 관리)
        setContent {
            LogTalkTheme {
                AppNavigation() // 여기서 내부적으로 hiltViewModel()을 호출하며 DB 사용됨
            }
        }
    }
}