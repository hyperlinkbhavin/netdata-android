package com.netdata.app.di

import android.app.Application
import com.netdata.app.di.component.ApplicationComponent
import com.netdata.app.di.component.DaggerApplicationComponent

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
