package com.example.logtalk.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "title")
data class TitleData(
    @PrimaryKey(autoGenerate = true)
    val titleId: Long = 0L,
    val title: String,
    val embedding: ByteArray?, // 1024차원 벡터 저장 (TypeConverter 필요)
    val createdAt: Long = System.currentTimeMillis(),
    val emotionPercentage: Float? = null // AI가 분석한 긍정 감정 퍼센트 (0~100)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TitleData

        if (titleId != other.titleId) return false
        if (createdAt != other.createdAt) return false
        if (title != other.title) return false
        if (!embedding.contentEquals(other.embedding)) return false
        if (emotionPercentage != other.emotionPercentage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titleId.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + embedding.contentHashCode()
        result = 31 * result + (emotionPercentage?.hashCode() ?: 0)
        return result
    }
}

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = TitleData::class,
            parentColumns = ["titleId"], // 부모 테이블의 컬럼
            childColumns = ["parentTitleId"], // 자식 테이블의 컬럼
            onDelete = ForeignKey.CASCADE // 부모가 삭제되면 자식도 삭제 (종속성 확보)
        )
    ],
    indices = [Index(value = ["parentTitleId"])] // FK 컬럼에 인덱스 설정 권장
)
data class MessageData(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0L,
    val parentTitleId: Long, // TitleEmbedding.titleId 참조
    val sender: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = false)
    val userName: String,
    val prompt: String,
    val recentSummarize: String,
    val onBoard: String,
    val insight: String
)
