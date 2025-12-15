package com.example.logtalk.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// SettingCard 컴포저블 (AI 페르소나, 데이터 관리 카드)
@Composable
fun SettingCard(
    title: String,
    icon: @Composable () -> Unit,
    descriptionComposable: @Composable () -> Unit, // Composable 블록으로 내용 영역을 받음
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showCancelButton: Boolean = false,
    onCancel: (() -> Unit)? = null,
    extraContent: (@Composable () -> Unit)? = null // 추가 컨텐츠
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                icon()
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    if (subtitle != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            descriptionComposable() // 내용 Composable 호출
            Spacer(Modifier.height(16.dp))

            if (showCancelButton && onCancel != null) {
                // 편집 모드: 저장과 취소 버튼을 나란히 배치
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = buttonText,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "취소",
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                // 일반 모드: 버튼 하나만
                if (buttonColor == Color.White) {
                    // 흰색 버튼은 OutlinedButton으로 표시
                    OutlinedButton(
                        onClick = onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = buttonText,
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = buttonText,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // 추가 컨텐츠 표시
            if (extraContent != null) {
                extraContent()
            }
        }
    }
}

// AppInfoCard 컴포저블 (앱 정보 카드)
@Composable
fun AppInfoCard(data: AppInfoData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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