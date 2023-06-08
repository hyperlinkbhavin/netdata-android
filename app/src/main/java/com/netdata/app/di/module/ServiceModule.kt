package com.netdata.app.di.module

import com.netdata.app.data.datasource.HomeLiveDataSource
import com.netdata.app.data.datasource.UserLiveDataSource
import com.netdata.app.data.repository.HomeRepository
import com.netdata.app.data.repository.UserRepository
import com.netdata.app.data.service.AuthenticationService
import com.netdata.app.data.service.HomeService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideUserRepository(userLiveDataSource: UserLiveDataSource): UserRepository {
        return userLiveDataSource
    }

    @Provides
    @Singleton
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(homeLiveDataSource: HomeLiveDataSource): HomeRepository {
        return homeLiveDataSource
    }

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

}