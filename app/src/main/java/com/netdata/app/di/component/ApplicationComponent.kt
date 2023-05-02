package com.netdata.app.di.component

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.res.Resources
import com.netdata.app.core.Session
import com.netdata.app.data.repository.UserRepository
import com.netdata.app.di.App
import com.netdata.app.di.module.ApplicationModule
import com.netdata.app.di.module.NetModule
import com.netdata.app.di.module.ServiceModule
import com.netdata.app.di.module.ViewModelModule
import com.netdata.app.utils.Validator
import dagger.BindsInstance
import dagger.Component
import java.io.File
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, NetModule::class, ServiceModule::class])
interface ApplicationComponent {

    fun context(): Context

    @Named("cache")
    fun provideCacheDir(): File

    fun provideResources(): Resources

    fun provideCurrentLocale(): Locale

    fun provideViewModelFactory(): ViewModelProvider.Factory

    fun inject(appShell: App)

    fun provideUserRepository(): UserRepository

    fun provideValidator():Validator

    fun provideSession(): Session

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun apiKey(@Named("api-key") apiKey: String): ApplicationComponentBuilder

        @BindsInstance
        fun application(application: Application): ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }

}
