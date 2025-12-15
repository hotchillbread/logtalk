package com.example.logtalk.core.utils

import android.util.Log

//싱글톤 객체, MainAvtivity에서 설정해주고
object Logger {
    private const val DEFAULT_TAG = "LogTalk"
    private var isDebug = false

    fun init(debug: Boolean) {
        isDebug = debug
    }

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (isDebug) Log.d(tag, message)
    }

    fun e(message: String, tag: String = DEFAULT_TAG) {
        if (isDebug) Log.e(tag, message)
    }
}