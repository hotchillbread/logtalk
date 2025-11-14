package com.example.logtalk.ui.theme

import android.view.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LogTalkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography().copy(
            bodyLarge = Typography().bodyLarge.copy(fontFamily = Inter),
            bodyMedium = Typography().bodyMedium.copy(fontFamily = Inter),
            titleLarge = Typography().titleLarge.copy(fontFamily = Inter),
            titleMedium = Typography().titleMedium.copy(fontFamily = Inter),
            labelLarge = Typography().labelLarge.copy(fontFamily = Inter)
        ),
        content = {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize(),

                content = content
            )
        }
    )
}

