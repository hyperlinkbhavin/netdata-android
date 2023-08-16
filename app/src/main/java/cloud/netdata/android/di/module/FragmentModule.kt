package cloud.netdata.android.di.module

import cloud.netdata.android.di.PerFragment
import cloud.netdata.android.ui.base.BaseFragment
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
