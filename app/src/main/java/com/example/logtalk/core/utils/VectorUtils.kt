package com.example.logtalk.core.utils

import kotlin.math.sqrt

fun calculateCosineSimilarity(vecA: List<Double>, vecB: List<Double>): Double {
    if (vecA.isEmpty() || vecB.isEmpty() || vecA.size != vecB.size) {
        return 0.0
    }

    var dotProduct = 0.0
    var normA = 0.0
    var normB = 0.0

    for (i in vecA.indices) {
        dotProduct += vecA[i] * vecB[i]
        normA += vecA[i] * vecA[i]
        normB += vecB[i] * vecB[i]
    }

    val denominator = sqrt(normA) * sqrt(normB)

    return if (denominator == 0.0) 0.0 else dotProduct / denominator
}