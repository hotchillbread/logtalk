package com.example.logtalk.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Title::class, MessageData::class, UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun titleDao(): TitleDao
    abstract fun messageDao(): MessageDao
}
