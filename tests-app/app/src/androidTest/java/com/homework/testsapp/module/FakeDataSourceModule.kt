package com.homework.testsapp.module

import com.homework.data.DataSource
import com.homework.data.di.DataSourceModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
abstract class FakeDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindDataSource(impl: FakeDataSource): DataSource
}