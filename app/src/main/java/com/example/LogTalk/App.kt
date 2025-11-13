package com.example.logtalk

import android.app.Application
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG)
    }
}