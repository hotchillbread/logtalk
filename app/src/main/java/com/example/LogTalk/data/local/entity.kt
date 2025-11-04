package com.example.logtalk.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "title")
data class Title(
    @PrimaryKey(autoGenerate = true)
    val titleId: Long = 0L,
    val title: String,
    val embedding: ByteArray, // 1024차원 벡터 저장 (TypeConverter 필요)
    val timestamp: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Title

        if (titleId != other.titleId) return false
        if (timestamp != other.timestamp) return false
        if (title != other.title) return false
        if (!embedding.contentEquals(other.embedding)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titleId.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Title::class,
            parentColumns = ["titleId"], // 부모 테이블의 컬럼
            childColumns = ["parentTitleId"], // 자식 테이블의 컬럼
            onDelete = ForeignKey.CASCADE // 부모가 삭제되면 자식도 삭제 (종속성 확보)
        )
    ],
    indices = [Index(value = ["parentTitleId"])] // FK 컬럼에 인덱스 설정 권장
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0L,
    val parentTitleId: Long, // TitleEmbedding.titleId 참조
    val sender: String,
    val content: String,
    val messageTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val userId: Int = 1, // 단일 항목을 위해 1로 고정
    val systemPrompt: String,
    val postsCount: Int,
    val messagesUsed: Int
)
