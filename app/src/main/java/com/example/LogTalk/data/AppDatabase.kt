package com.example.logtalk.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.data.local.ChatDao
import com.example.logtalk.data.local.MessageData
import com.example.logtalk.data.local.Title
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.data.local.UserDao
import com.example.logtalk.data.local.UserProfile

@Database(
    entities = [Title::class, MessageData::class, UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    //Dao 정의
    abstract fun userDao(): UserDao
    abstract fun titleDao(): TitleDao
    abstract fun messageDao(): ChatDao
    companion object {
        @Volatile // 모든 스레드에서 즉시 변경 사항을 볼 수 있도록
        private var INSTANCE: AppDatabase? = null

        //db 연결

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // 마이그레이션 -> 해야될지는 모르겠어요
                    .fallbackToDestructiveMigration()
                    .build()

                Logger.d("db 연결 성공")
                INSTANCE = instance
                instance
            }
        }
    }
}