package com.example.logtalk.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    // 각 UseCase들은 이미 @Inject constructor를 가지고 있어서
    // Hilt가 자동으로 의존성을 주입합니다.
    // 별도의 provide 함수가 필요하지 않습니다.
}

