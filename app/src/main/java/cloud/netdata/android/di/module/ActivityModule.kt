package cloud.netdata.android.di.module

import cloud.netdata.android.di.PerActivity
import cloud.netdata.android.ui.base.BaseActivity
import cloud.netdata.android.ui.manager.FragmentHandler
import cloud.netdata.android.ui.manager.FragmentManager
import cloud.netdata.android.ui.manager.Navigator

import javax.inject.Named

import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    @PerActivity
    internal fun navigator(activity: BaseActivity): Navigator {
        return activity
    }

    @Provides
    @PerActivity
    internal fun fragmentManager(baseActivity: BaseActivity): androidx.fragment.app.FragmentManager {
        return baseActivity.supportFragmentManager
    }

    @Provides
    @PerActivity
    @Named("placeholder")
    internal fun placeHolder(baseActivity: BaseActivity): Int {
        return baseActivity.findFragmentPlaceHolder()
    }

    @Provides
    @PerActivity
    internal fun fragmentHandler(fragmentManager: FragmentManager): FragmentHandler {
        return fragmentManager
    }


}
