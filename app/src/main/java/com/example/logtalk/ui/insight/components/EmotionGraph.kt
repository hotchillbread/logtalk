package com.example.logtalk.ui.insight.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logtalk.ui.insight.EmotionDataPoint

@Composable
fun EmotionGraph(
    data: List<EmotionDataPoint>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "데이터가 없습니다",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val graphWidth = width - padding * 2
        val graphHeight = height - padding * 2

        // Y축 범위 (0-100%)
        val minY = 0f
        val maxY = 100f

        // 데이터 포인트들
        val points = data.mapIndexed { index, point ->
            val x = padding + (graphWidth / (data.size - 1)) * index
            val normalizedY = (point.positivePercentage - minY) / (maxY - minY)
            val y = padding + graphHeight * (1 - normalizedY)
            Offset(x, y)
        }

        // 그라데이션 영역 그리기
        val gradientPath = Path().apply {
            moveTo(points.first().x, height - padding)
            points.forEach { point ->
                lineTo(point.x, point.y)
            }
            lineTo(points.last().x, height - padding)
            close()
        }

        drawPath(
            path = gradientPath,
            color = Color(0xFF6282E1).copy(alpha = 0.2f),
            style = Fill
        )

        // 라인 그리기
        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFF6282E1),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3f
            )
        }

        // 포인트 그리기
        points.forEach { point ->
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = point
            )
            drawCircle(
                color = Color(0xFF6282E1),
                radius = 4f,
                center = point
            )
        }

        // X축 라벨 (날짜)
        data.forEachIndexed { index, point ->
            val x = padding + (graphWidth / (data.size - 1)) * index
            val textLayoutResult = textMeasurer.measure(
                text = point.date,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = x - textLayoutResult.size.width / 2,
                    y = height - padding + 10
                )
            )
        }

        // Y축 라벨 (퍼센트)
        val yLabels = listOf(0, 25, 50, 75, 100)
        yLabels.forEach { label ->
            val normalizedY = (label - minY) / (maxY - minY)
            val y = padding + graphHeight * (1 - normalizedY)

            // 가이드 라인
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )

            // 라벨
            val textLayoutResult = textMeasurer.measure(
                text = "$label%",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 5f,
                    y = y - textLayoutResult.size.height / 2
                )
            )
        }
    }
}

