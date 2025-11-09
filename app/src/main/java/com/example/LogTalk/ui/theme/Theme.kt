package com.example.logtalk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

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
        content = content
    )
}