package cloud.netdata.android.di.component

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.res.Resources
import cloud.netdata.android.core.AppPreferences
import cloud.netdata.android.core.Session
import cloud.netdata.android.data.repository.UserRepository
import cloud.netdata.android.di.App
import cloud.netdata.android.di.module.ApplicationModule
import cloud.netdata.android.di.module.NetModule
import cloud.netdata.android.di.module.ServiceModule
import cloud.netdata.android.di.module.ViewModelModule
import cloud.netdata.android.utils.Validator
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

    fun provideAppPreference(): AppPreferences

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun apiKey(@Named("api-key") apiKey: String): ApplicationComponentBuilder

        @BindsInstance
        fun application(application: Application): ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }

}
