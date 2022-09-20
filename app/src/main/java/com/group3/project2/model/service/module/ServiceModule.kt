package com.group3.project2.model.service.module

import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.FunctionService
import com.group3.project2.model.service.LogService
import com.group3.project2.model.service.StorageService
import com.group3.project2.model.service.impl.AccountServiceImpl
import com.group3.project2.model.service.impl.FunctionServiceImpl
import com.group3.project2.model.service.impl.LogServiceImpl
import com.group3.project2.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideFunctionService(impl: FunctionServiceImpl): FunctionService
}