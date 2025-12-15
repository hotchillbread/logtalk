package com.example.logtalk.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onNavigateToKeywordSelection: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showKeywordEditWarning by remember { mutableStateOf(false) }

    // 키워드 편집 경고 다이얼로그
    if (showKeywordEditWarning) {
        AlertDialog(
            onDismissRequest = { showKeywordEditWarning = false },
            title = {
                Text(
                    text = "프롬프트 초기화",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = "현재 프롬프트가 초기화됩니다.\n계속하시겠습니까?",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF424242)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showKeywordEditWarning = false
                        onNavigateToKeywordSelection()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6282E1),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showKeywordEditWarning = false },
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = "취소",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF757575)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 0.dp
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp)
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
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    text = "Talk",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6282E1) // 파란색
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
                Column {
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
                }
            },

            // 버튼 텍스트와 색상 분기 (저장/편집 모드)
            buttonText = if (uiState.isEditingPersona) "저장" else "편집",
            buttonColor = if (uiState.isEditingPersona) Color.Black else Color.White,
            showCancelButton = uiState.isEditingPersona,
            onCancel = if (uiState.isEditingPersona) {
                { viewModel.sendIntent(SettingsIntent.CancelEdit) }
            } else null,

            onClick = {
                if (uiState.isEditingPersona) {
                    // 저장 시 현재 편집 중인 임시 데이터를 전달 (SavePersona Intent)
                    viewModel.sendIntent(SettingsIntent.SavePersona(uiState.currentEditingPersona))
                } else {
                    // 편집 모드 진입 (ClickEditPersona Intent)
                    viewModel.sendIntent(SettingsIntent.ClickEditPersona)
                }
            },

            // 키워드 설정 버튼 추가 (편집 버튼 아래)
            extraContent = if (!uiState.isEditingPersona) {
                {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            // 기본 프롬프트가 아니면 경고창 표시
                            val defaultPrompt = "당신은 친절하고 공감 능력이 뛰어난 심리 상담 AI입니다. 사용자의 이야기를 경청하고, 따뜻한 조언을 제공하며, 항상 긍정적이고 지지적인 태도를 유지합니다."
                            if (uiState.persona.description != defaultPrompt && uiState.persona.description.isNotBlank()) {
                                showKeywordEditWarning = true
                            } else {
                                onNavigateToKeywordSelection()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6282E1),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "키워드로 페르소나 설정하기",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else null
        )

        Spacer(Modifier.height(16.dp))

        // 데이터 관리 카드
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

        // 삭제 확인 다이얼로그
        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.sendIntent(SettingsIntent.DismissDialog) },
                title = {
                    Text(
                        text = "정말 삭제하시겠습니까?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        text = "이 작업은 되돌릴 수 없습니다.\n모든 상담 기록이 영구적으로 삭제됩니다.",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(0xFF424242)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.sendIntent(SettingsIntent.ConfirmDelete) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5252),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "삭제",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.sendIntent(SettingsIntent.DismissDialog) },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "취소",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF757575)
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                tonalElevation = 0.dp
            )
        }
        }
    }
}