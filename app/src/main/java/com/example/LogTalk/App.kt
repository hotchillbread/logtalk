package com.example.logtalk

import android.app.Application
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.BuildConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG)
    }
}