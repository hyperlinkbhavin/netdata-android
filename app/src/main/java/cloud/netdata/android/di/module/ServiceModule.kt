package cloud.netdata.android.di.module

import cloud.netdata.android.data.datasource.HomeLiveDataSource
import cloud.netdata.android.data.datasource.UserLiveDataSource
import cloud.netdata.android.data.repository.HomeRepository
import cloud.netdata.android.data.repository.UserRepository
import cloud.netdata.android.data.service.AuthenticationService
import cloud.netdata.android.data.service.HomeService
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