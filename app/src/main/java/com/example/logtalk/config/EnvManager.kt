package com.example.logtalk.config

import com.example.logtalk.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.example.logtalk.core.utils.Logger

object EnvManager {
    private const val TAG = "EnvManager"
    private val remoteConfig = Firebase.remoteConfig

    // 초기화
    //onComplete 람다함수로 비동기 콜백 구현
    fun initialize(onComplete: (isSuccessful: Boolean) -> Unit) {
        //debug시 1분마다(로깅용), 앱 배포시 1시간마다 env 받기
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchAndActivate(onComplete)
    }

    private fun fetchAndActivate(onComplete: (isSuccessful: Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Logger.d("Remote Config 패치 성공. 업데이트 여부: $updated")
                    Logger.d("API Base URL: ${getOpenaiApiKey()}")

                    onComplete(true)
                } else {
                    Logger.e("Remote Config 패치 실패")

                    onComplete(false)
                }
            }
    }

    // api key가져오기
    fun getOpenaiApiKey(): String {
        return BuildConfig.OPENAI_API_KEY
    }
}