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
@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EnvManager.initialize()

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
