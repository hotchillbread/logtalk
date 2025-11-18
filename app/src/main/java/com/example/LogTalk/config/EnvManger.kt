package com.example.logtalk.config

import com.example.logtalk.BuildConfig
import com.example.logtalk.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.example.logtalk.core.utils.Logger

object EnvManager {
    private const val TAG = "EnvManager"

    // Remote Config 인스턴스 초기화
    private val remoteConfig = Firebase.remoteConfig

    // 초기화 및 값 가져오기
    fun initialize() {
        // 1. 개발자 모드 설정 (개발 중에는 빠른 테스트를 위해 캐시 만료 시간을 짧게 설정)
        val configSettings = remoteConfigSettings {
            // 운영 환경에서는 3600초(1시간) 등 긴 값 사용
            // 개발 환경에서는 0을 사용하여 매번 가져오도록 설정 (권장하지 않음, 디버깅 용도)
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // 2. 앱 내 기본값 설정
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.d("Remote Config 기본값 설정 성공")
                } else {
                    Logger.e("Remote Config 기본값 설정 실패")
                }

                // 3. 서버에서 최신 값 가져오기 및 활성화
                fetchAndActivate()
            }
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Logger.d("Remote Config 패치 성공. 업데이트 여부: $updated")
                    // 값 사용
                    Logger.d("API Base URL: ${getApiBaseUrl()}")

                } else {
                    Logger.e("Remote Config 패치 실패")
                }
            }
    }

    // 환경 변수를 가져오는 함수들
    fun getApiBaseUrl(): String {
        return remoteConfig.getString("api_base_url")
    }

    fun isFeatureFlagEnabled(): Boolean {
        return remoteConfig.getBoolean("feature_flag_enabled")
    }
}