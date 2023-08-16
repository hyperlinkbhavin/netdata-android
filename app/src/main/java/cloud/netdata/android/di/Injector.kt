package cloud.netdata.android.di

import android.app.Application
import cloud.netdata.android.di.component.ApplicationComponent
import cloud.netdata.android.di.component.DaggerApplicationComponent

enum class Injector private constructor() {
    INSTANCE;

    lateinit var applicationComponent: ApplicationComponent
        internal set

    fun initAppComponent(application: Application, apiKey: String) {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(application)
                .apiKey(apiKey)
                .build()
    }
}
