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
    fun initialize() {
        //debug시 5분마다, 앱 배포시 1시간마다 env 받기
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Logger.d("Remote Config 패치 성공. 업데이트 여부: $updated")
                    Logger.d("API Base URL: ${getOpenaiApiKey()}")

                } else {
                    Logger.e("Remote Config 패치 실패")
                }
            }
    }

    // api key가져오기
    fun getOpenaiApiKey(): String {
        return remoteConfig.getString("openai_api_key_url")
    }
}