package com.example.logtalk.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete


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
interface MessageDao {
    @Query("SELECT * FROM messages WHERE ParentTitleId = :parentTitleId ORDER BY createdAt")
    fun getAll(parentTitleId: Int): MessageData
}

