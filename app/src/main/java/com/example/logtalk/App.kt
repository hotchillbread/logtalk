package com.example.logtalk

import android.app.Application
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.BuildConfig
import com.google.android.gms.tasks.Tasks.await
import dagger.hilt.android.HiltAndroidApp
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.DependencyContainer
import com.example.logtalk.data.AppDatabase

@HiltAndroidApp
class App : Application() {

    //의존성 주입
    lateinit var container: DependencyContainer
    //db 연결, 전역에서 db 사용 가능
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG)

        EnvManager.initialize { isSuccessful -> 
            if (isSuccessful) {
                container = DependencyContainer(EnvManager)
                Logger.d("Dependency 초기화 완료")
            } else {
                Logger.e("Remote config 로드 실패 DI 컨테이너 의존성 주입 안됨 ㅠ (다시 켜보든가)")
            }
        }
    }
}