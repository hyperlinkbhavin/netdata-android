package cloud.netdata.android.di.component

import cloud.netdata.android.di.PerActivity
import cloud.netdata.android.di.module.ActivityModule
import cloud.netdata.android.di.module.FragmentModule
import cloud.netdata.android.ui.auth.AuthActivity
import cloud.netdata.android.ui.auth.IsolatedFullActivity
import cloud.netdata.android.ui.base.BaseActivity
import cloud.netdata.android.ui.home.HomeActivity
import cloud.netdata.android.ui.manager.Navigator
import cloud.netdata.android.ui.splash.SplashActivity

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
