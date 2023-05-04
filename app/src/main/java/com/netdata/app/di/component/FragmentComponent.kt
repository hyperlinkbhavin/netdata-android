package com.netdata.app.di.component

import com.netdata.app.di.PerFragment
import com.netdata.app.di.module.FragmentModule
import com.netdata.app.ui.auth.fragment.LoginFragment
import com.netdata.app.ui.auth.fragment.QRCodeLoginFragment
import com.netdata.app.ui.auth.fragment.WelcomeFragment
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.fragment.ChooseSpaceFragment
import com.netdata.app.ui.home.fragment.HomeFragment
import com.netdata.app.ui.notification.fragment.NotificationFragment
import com.netdata.app.ui.settings.fragment.*
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {
    fun baseFragment(): BaseFragment<*>
    fun inject(loginFragment: LoginFragment)
    fun inject(qrCodeLoginFragment: QRCodeLoginFragment)
    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(chooseSpaceFragment: ChooseSpaceFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(maintenanceModeSettingsFragment: MaintenanceModeSettingsFragment)
    fun inject(notificationPrioritySettingsFragment: NotificationPrioritySettingsFragment)
    fun inject(changeThemeFragment: ChangeThemeFragment)
    fun inject(termsAndConditionsFragment: TermsAndConditionsFragment)
    fun inject(homeFragment: HomeFragment)

}
