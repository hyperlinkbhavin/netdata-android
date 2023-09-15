package cloud.netdata.android.di.component

import cloud.netdata.android.di.PerFragment
import cloud.netdata.android.di.module.FragmentModule
import cloud.netdata.android.ui.auth.fragment.LoginFragment
import cloud.netdata.android.ui.auth.fragment.QRCodeLoginFragment
import cloud.netdata.android.ui.auth.fragment.WelcomeFragment
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.home.fragment.ChooseSpaceFragment
import cloud.netdata.android.ui.home.fragment.HomeDetailsFragment
import cloud.netdata.android.ui.home.fragment.HomeFragment
import cloud.netdata.android.ui.notification.fragment.NotificationFragment
import cloud.netdata.android.ui.settings.fragment.*
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
    fun inject(homeDetailsFragment: HomeDetailsFragment)
    fun inject(changeThemeDayFragment: ChangeThemeDayFragment)
    fun inject(changeThemeNightFragment: ChangeThemeNightFragment)
    fun inject(notificationRetentionSettingsFragment: NotificationRetentionSettingsFragment)
    fun inject(duplicateMaintenanceModeSettingsFragment: DuplicateMaintenanceModeSettingsFragment)

}
