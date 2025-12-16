package com.example.logtalk.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Repository 바인딩을 위한 Hilt Module
 * 현재는 AppModule에서 모든 Repository를 바인딩하고 있으므로 비어있습니다.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // SessionRepository는 AppModule에서 바인딩됩니다.
}

