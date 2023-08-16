package cloud.netdata.android.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.di.ViewModelKey
import cloud.netdata.android.ui.auth.viewmodel.LoginViewModel
import cloud.netdata.android.ui.base.ViewModelFactory
import cloud.netdata.android.ui.home.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}