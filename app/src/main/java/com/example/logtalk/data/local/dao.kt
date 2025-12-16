package com.example.logtalk.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow



@Dao
interface MessageDao {

    //메세지 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message:MessageData): Long

    //특정 채팅방 메세지 가져오기 (id기준)
    @Query("SELECT * FROM messages WHERE parentTitleId = :parentTitleId ORDER BY createdAt ASC")
    fun getMessagesByParentTitleId(parentTitleId: Long): Flow<List<MessageData>>


    //채팅방 삭제
    @Query("DELETE FROM messages WHERE parentTitleId = :parentTitleId")
    suspend fun deleteMessagesByParentTitleId(parentTitleId: Long)

    //첫번째 메시지 가져오기(제목 용)
    @Query("SELECT content FROM messages WHERE parentTitleId = :parentTitleId ORDER BY createdAt ASC LIMIT 1")
    suspend fun getFirstMessageContent(parentTitleId: Long): String?

    //메세지 몇개인지 기록 수 가져오기 근데 이거 왜만들었지...
    @Query("SELECT COUNT(messageId) FROM messages WHERE parentTitleId = :parentTitleId")
    suspend fun getMessageCount(parentTitleId: Long): Int

    //특정 세션의 사용자 메시지 개수만 가져오기 (AI 메시지 제외)
    @Query("SELECT COUNT(messageId) FROM messages WHERE parentTitleId = :parentTitleId AND sender = 'User'")
    suspend fun getUserMessageCount(parentTitleId: Long): Int

    //전체 사용자 메시지 개수 가져오기 (Groomy용 - AI 답변 제외)
    @Query("SELECT COUNT(*) FROM messages WHERE sender = 'User'")
    fun getTotalMessageCount(): Flow<Int>

    //전체 사용자 메시지 개수 가져오기 (suspend 버전 - 인사이트용)
    @Query("SELECT COUNT(*) FROM messages WHERE sender = 'User'")
    suspend fun getTotalUserMessageCount(): Int
}

@Dao
interface TitleDao {

    //새 채팅방 제목 저장 (새 채팅 시작 시 호출)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: TitleData): Long //pk반환

    //전체 채팅방 목록 가져오기 (정렬은 최신순)
    @Query("SELECT * FROM title ORDER BY createdAt DESC")
    fun getAllTitles(): Flow<List<TitleData>>

    //채팅방 삭제 (ForeignKey.CASCADE로 MessageData도 같이 삭제됨)
    @Delete
    suspend fun deleteTitle(title: TitleData)

    //채팅방 제목 업데이트 (LLM으로 제목 요약 후 저장)
    @Query("UPDATE title SET title = :newTitle WHERE titleId = :titleId")
    suspend fun updateTitleText(titleId: Long, newTitle: String)

    // 5. 임베딩 벡터를 사용하여 비슷한 상담 찾기 (벡터 검색 쿼리)
    @Query("SELECT * FROM title WHERE titleId != :currentTitleId")
    suspend fun getAllEmbeddingsExceptCurrent(currentTitleId: Long): List<TitleData>

    @Query("SELECT * FROM title WHERE titleId = :titleId")
    suspend fun getTitleById(titleId: Long): TitleData?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTitle(title: TitleData)
}

@Dao
interface UserDao {

    //프로필 정보 저장/업데이트 -> 임시로 만들어둠 나경님이 만들거로 업데이트 해야함
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    // 현재사용자 프로필 정보 가져오기
    @Query("SELECT * FROM profile WHERE userName = :userName")
    fun getUserProfile(userName: String): Flow<UserProfile?>

    // 시스템 프롬프트만 업데이트
    @Query("UPDATE profile SET prompt = :newPrompt WHERE userName = :userName")
    suspend fun updatePrompt(userName: String, newPrompt: String)
}