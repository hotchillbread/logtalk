package com.example.logtalk

import android.app.Application
import com.example.logtalk.core.utils.Logger
import dagger.hilt.android.HiltAndroidApp
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.DependencyContainer
import com.example.logtalk.data.AppDatabase

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.init(BuildConfig.DEBUG)

        // EnvManager 초기화
        EnvManager.initialize { isSuccessful ->
            if (isSuccessful) {
                Logger.d("EnvManager 초기화 완료")
                // TODO: 이 시점에 API Key를 사용하여 Hilt 모듈을 초기화하는 추가 로직이 필요할 수 있습니다.
            } else {
                Logger.e("Remote config 로드 실패.")
            }
        }
    }
}