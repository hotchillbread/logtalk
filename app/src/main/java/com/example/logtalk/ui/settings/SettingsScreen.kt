package com.example.logtalk.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onPersonaEditClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            // 왼쪽: 뒤로가기 아이콘 + 설정 텍스트
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "설정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // 중앙: LogTalk 로고
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Talk",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6282E1)
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

        // AI 페르소나 설정 카드
        SettingCard(
            title = "AI 페르소나 설정",
            subtitle = "AI의 성격과 대화 스타일을 설정합니다",
            icon = { Icon(Icons.Default.Person, contentDescription = "페르소나 아이콘") },

            // description Composable 분기
            descriptionComposable = {
                if (uiState.isEditingPersona) {
                    // 편집 모드: 텍스트 입력 필드
                    TextField(
                        value = uiState.currentEditingPersona.description,
                        onValueChange = { newDesc ->
                            viewModel.sendIntent(SettingsIntent.UpdateEditingDescription(newDesc))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    // 읽기 모드
                    Text(
                        text = uiState.persona.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            },

            // 버튼 텍스트와 색상 분기
            buttonText = "키워드 설정",
            buttonColor = Color.White,
            showCancelButton = false,
            onCancel = null,

            onClick = {
                // 키워드 선택 화면으로 이동
                onPersonaEditClick()
            }
        )

        // 취소 버튼은 이제 SettingCard 내부에서 처리되므로 제거

        Spacer(Modifier.height(16.dp))

        // 데이터 관리 카드 (4단계에서 로직 완성)
        SettingCard(
            title = "데이터 관리",
            icon = { Icon(Icons.Default.Warning, contentDescription = "경고 아이콘") },
            descriptionComposable = {
                Text("모든 상담 기록을 삭제합니다", style = MaterialTheme.typography.bodySmall)
            },
            buttonText = "모든 기록 삭제",
            buttonColor = Color.Red,
            onClick = { viewModel.sendIntent(SettingsIntent.ShowDeleteDialog) } // 4단계 로직 연결
        )

        Spacer(Modifier.height(16.dp))

        // 앱 정보 카드
        AppInfoCard(data = uiState.appInfo)

        // 삭제 확인 다이얼로그 (4단계에서 구현)
        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.sendIntent(SettingsIntent.DismissDialog) },
                title = { Text("정말 삭제하시겠습니까?") },
                text = { Text("이 작업은 되돌릴 수 없습니다. 모든 상담 기록이 영구적으로 삭제됩니다.") },
                confirmButton = {
                    Button(
                        onClick = { viewModel.sendIntent(SettingsIntent.ConfirmDelete) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("삭제")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.sendIntent(SettingsIntent.DismissDialog) }) {
                        Text("취소")
                    }
                }
            )
        }
        }
    }
}