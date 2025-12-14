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

        //db 연결
        val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

        //env 의존성 주입
        EnvManager.initialize { isSuccessful ->
            if (isSuccessful) {
                Logger.d("API Key 준비됨.")
            } else {
                Logger.e("초기화 실패. 기본 설정으로 앱을 실행합니다.")
                throw RuntimeException("앱의 환경설정 로드 실패")
            }
        }

        //하단 컨트롤러 숨기기(
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        Logger.d("app start")
        enableEdgeToEdge()
        setContent {
            LogTalkTheme {
                AppNavigation()
            }
        }
    }
}
