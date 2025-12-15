package com.example.logtalk.core.di

import com.example.logtalk.data.repositoryImpl.SettingsRepositoryImpl
import com.example.logtalk.ui.settings.DeleteAllRecordsUseCase
import com.example.logtalk.ui.settings.LoadPersonaUseCase
import com.example.logtalk.ui.settings.SavePersonaUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    // 💡 SavePersonaUseCase를 SettingsRepositoryImpl에 바인딩
    @Binds
    @Singleton
    abstract fun bindSavePersonaUseCase(
        impl: SettingsRepositoryImpl
    ): SavePersonaUseCase

    // 💡 LoadPersonaUseCase 바인딩
    @Binds
    @Singleton
    abstract fun bindLoadPersonaUseCase(
        impl: SettingsRepositoryImpl
    ): LoadPersonaUseCase

    // 💡 DeleteAllRecordsUseCase 바인딩
    @Binds
    @Singleton
    abstract fun bindDeleteAllRecordsUseCase(
        impl: SettingsRepositoryImpl
    ): DeleteAllRecordsUseCase
}