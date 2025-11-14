package com.example.logtalk.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        // AI 페르소나 설정 카드
        SettingCard(
            title = "AI 페르소나 설정",
            icon = { Icon(Icons.Default.Person, contentDescription = "페르소나 아이콘") },

            // description Composable 분기
            descriptionComposable = {
                if (uiState.isEditingPersona) {
                    // 편집 모드: 텍스트 입력 필드
                    OutlinedTextField(
                        value = uiState.currentEditingPersona.description,
                        onValueChange = { newDesc ->
                            viewModel.sendIntent(SettingsIntent.UpdateEditingDescription(newDesc))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        label = { Text("페르소나 내용") },
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                } else {
                    // 읽기 모드
                    Text(
                        text = uiState.persona.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },

            // 버튼 텍스트와 색상 분기 (저장/편집 모드)
            buttonText = if (uiState.isEditingPersona) "저장" else "편집",
            buttonColor = if (uiState.isEditingPersona) Color.Black else MaterialTheme.colorScheme.primary,

            onClick = {
                if (uiState.isEditingPersona) {
                    // 저장 시 현재 편집 중인 임시 데이터를 전달 (SavePersona Intent)
                    viewModel.sendIntent(SettingsIntent.SavePersona(uiState.currentEditingPersona))
                } else {
                    // 편집 모드 진입 (ClickEditPersona Intent)
                    viewModel.sendIntent(SettingsIntent.ClickEditPersona)
                }
            }
        )

        // 취소 버튼 (편집 모드에서만 표시)
        if (uiState.isEditingPersona) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.sendIntent(SettingsIntent.CancelEdit) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("취소")
            }
        }

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