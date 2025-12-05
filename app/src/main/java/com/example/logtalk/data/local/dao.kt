package com.example.logtalk.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM profile")
    fun getAll(): UserProfile

}

@Dao
interface TitleDao {
    @Query("SELECT * FROM title")
    fun getAll(): Title
}

@Dao
interface ChatDao {
    // --- MessageData 관련 ---
    @Query("SELECT * FROM messages WHERE parentTitleId = :titleId ORDER BY createdAt ASC")
    fun getMessagesFlow(titleId: Long): Flow<List<MessageData>>

    @Query("SELECT * FROM messages WHERE parentTitleId = :titleId ORDER BY createdAt ASC")
    suspend fun getMessagesSync(titleId: Long): List<MessageData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageData)

    // --- Title 관련 ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: Title): Long // 생성된 ID 반환

    @Query("SELECT * FROM title WHERE titleId = :titleId")
    suspend fun getTitle(titleId: Long): Title?

    // --- UserProfile 관련 ---
    @Query("SELECT * FROM profile LIMIT 1") // 프로필은 1개라고 가정
    suspend fun getUserProfile(): UserProfile?
}

