package com.netdata.app.di.module

import com.netdata.app.di.PerActivity
import com.netdata.app.ui.base.BaseActivity
import com.netdata.app.ui.manager.FragmentHandler
import com.netdata.app.ui.manager.Navigator

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
    internal fun fragmentHandler(fragmentManager: com.netdata.app.ui.manager.FragmentManager): FragmentHandler {
        return fragmentManager
    }


}
