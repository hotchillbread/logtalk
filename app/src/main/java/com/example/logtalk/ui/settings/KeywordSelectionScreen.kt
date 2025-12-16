package com.example.logtalk.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logtalk.ui.theme.LoginColors

/**
 * 키워드 선택 기반 페르소나 설정 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordSelectionScreen(
    viewModel: PersonaKeywordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val selectedKeywords by viewModel.selectedKeywords.collectAsState()
    val currentPrompt by viewModel.currentPrompt.collectAsState()
    val keywordsByCategory = remember(viewModel.availableKeywords) {
        viewModel.getKeywordsByCategory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바 (설정 화면 스타일)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // 왼쪽: 뒤로가기 아이콘 + 타이틀
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "AI 페르소나 설정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // 오른쪽: 모두 지우기 버튼
            IconButton(
                onClick = { viewModel.clearAllKeywords() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "모두 지우기",
                    tint = LoginColors.TextGray
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 전체 스크롤 영역 (키워드 + 프롬프트)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp) // 하단 네비게이션 바 + 여유 공간
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "원하는 키워드를 선택하여 AI의 성격을 설정하세요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LoginColors.TextGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 카테고리별로 키워드 표시
                keywordsByCategory.forEach { (category, keywords) ->
                    item {
                        CategoryKeywordSection(
                            category = category,
                            keywords = keywords,
                            selectedKeywords = selectedKeywords,
                            onKeywordToggle = { viewModel.toggleKeyword(it) }
                        )
                    }
                }

                // 프롬프트 미리보기도 LazyColumn 안에 포함
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    PromptPreviewCard(
                        currentPrompt = currentPrompt,
                        onSaveClick = {
                            viewModel.savePrompt()
                            onNavigateBack()
                        }
                    )
                }
            }
        }
    }
}

/**
 * 카테고리별 키워드 섹션
 */
@Composable
fun CategoryKeywordSection(
    category: String,
    keywords: List<PersonaKeyword>,
    selectedKeywords: Set<String>,
    onKeywordToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 카테고리 타이틀
        Text(
            text = category,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 키워드 Chip들
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            keywords.forEach { keyword ->
                KeywordChip(
                    keyword = keyword,
                    isSelected = selectedKeywords.contains(keyword.name),
                    onToggle = { onKeywordToggle(keyword.name) }
                )
            }
        }
    }
}

/**
 * 키워드 Chip 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordChip(
    keyword: PersonaKeyword,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onToggle,
        label = {
            Text(
                keyword.name,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "선택됨",
                    modifier = Modifier.size(18.dp),
                    tint = LoginColors.Primary
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            // 선택됨 - 파란색 배경
            selectedContainerColor = Color(0xFFEFF3FB), // 매우 연한 파란색
            selectedLabelColor = LoginColors.Primary,

            // 선택 안 됨 - 흰색 배경
            containerColor = Color.White,
            labelColor = Color.Black
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            selectedBorderColor = LoginColors.Primary,
            borderColor = Color(0xFFE0E0E0),
            selectedBorderWidth = 1.5.dp,
            borderWidth = 1.dp
        )
    )
}

/**
 * 프롬프트 미리보기 및 저장 카드
 */
@Composable
fun PromptPreviewCard(
    currentPrompt: String,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 타이틀
            Text(
                text = "자동 생성된 프롬프트",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFF0F0F0)
            )

            // 프롬프트 내용 (스크롤 가능)
            if (currentPrompt.isBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "키워드를 선택하면 프롬프트가 생성됩니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LoginColors.TextGray
                    )
                }
            } else {
                // 프롬프트 텍스트 - 높이 제한 없이 모두 표시
                Text(
                    text = currentPrompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 저장 버튼
            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = currentPrompt.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginColors.Primary,
                    contentColor = Color.White,
                    disabledContainerColor = LoginColors.TextGray.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "프롬프트 저장 및 적용",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

