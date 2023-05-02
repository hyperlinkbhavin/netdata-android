package com.netdata.app.di.module

import com.netdata.app.di.PerFragment
import com.netdata.app.ui.base.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val baseFragment: BaseFragment<*>) {

    @Provides
    @PerFragment
    internal fun provideBaseFragment(): BaseFragment<*> {
        return baseFragment
    }

}
