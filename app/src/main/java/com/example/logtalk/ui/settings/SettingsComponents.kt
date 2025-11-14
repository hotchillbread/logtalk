package com.example.logtalk.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// SettingCard 컴포저블 (AI 페르소나, 데이터 관리 카드)
@Composable
fun SettingCard(
    title: String,
    icon: @Composable () -> Unit,
    descriptionComposable: @Composable () -> Unit, // Composable 블록으로 내용 영역을 받음
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                icon()
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    descriptionComposable() // 내용 Composable 호출
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(buttonText, color = Color.White)
            }
        }
    }
}

// AppInfoCard 컴포저블 (앱 정보 카드)
@Composable
fun AppInfoCard(data: AppInfoData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("앱 정보", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            AppInfoRow("버전", data.version)
            AppInfoRow("개발자", data.developer)
            AppInfoRow("문의", data.contact)
        }
    }
}

@Composable
private fun AppInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}