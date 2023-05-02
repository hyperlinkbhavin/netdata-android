package com.netdata.app.di.component

import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.di.PerActivity
import com.netdata.app.di.module.ActivityModule
import com.netdata.app.di.module.FragmentModule
import com.netdata.app.ui.auth.AuthActivity
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseActivity
import com.netdata.app.ui.manager.Navigator
import com.netdata.app.ui.splash.SplashActivity

import dagger.BindsInstance
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun activity(): BaseActivity

    fun navigator(): Navigator


    operator fun plus(fragmentModule: FragmentModule): FragmentComponent

    fun inject(mainActivity: HomeActivity)
    fun inject(mainActivity: SplashActivity)
    fun inject(authActivity: AuthActivity)
    fun inject(isolatedFullActivity: IsolatedFullActivity)

    @Component.Builder
    interface Builder {

        fun bindApplicationComponent(applicationComponent: ApplicationComponent): Builder

        @BindsInstance
        fun bindActivity(baseActivity: BaseActivity): Builder

        fun build(): ActivityComponent
    }
}
