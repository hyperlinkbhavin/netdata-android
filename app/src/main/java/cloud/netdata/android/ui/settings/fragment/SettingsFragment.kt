package cloud.netdata.android.ui.settings.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.ThemeMode
import cloud.netdata.android.data.pojo.request.SettingsList
import cloud.netdata.android.databinding.SettingsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.auth.AuthActivity
import cloud.netdata.android.ui.auth.IsolatedFullActivity
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.settings.adapter.SettingsAdapter
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.localdb.DatabaseHelper

class SettingsFragment : BaseFragment<SettingsFragmentBinding>() {

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    lateinit var dbHelper: DatabaseHelper
    lateinit var customDialog: AlertDialog

    private val settingsAdapter by lazy {
        SettingsAdapter() { view, position, item ->
            when (view.id) {
                R.id.constraintMain -> {
                    when (position) {
                        // Maintenance Mode Settings
                        0 -> {
                            navigator.load(MaintenanceModeSettingsFragment::class.java)
                                .replace(true)
                        }

                        // Notification Priority Settings
                        1 -> {
                            navigator.load(NotificationPrioritySettingsFragment::class.java)
                                .replace(true)
                        }

                        // Notification Retention Settings
                        2 -> {
                            navigator.load(NotificationRetentionSettingsFragment::class.java).replace(true)
                        }

                        // Share App
                        3 -> {
                            shareApp()
                        }

                        // Terms and Privacy Policy
                        4 -> {
                            navigator.load(TermsAndConditionsFragment::class.java).replace(true)
                        }

                        // Change Your Theme
                        5 -> {
                            if(appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Night.name
                                || appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Day.name){
                                if (appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Night.name) {
                                    navigator.loadActivity(
                                        IsolatedFullActivity::class.java,
                                        ChangeThemeNightFragment::class.java
                                    ).start()
                                } else {
                                    navigator.loadActivity(
                                        IsolatedFullActivity::class.java,
                                        ChangeThemeDayFragment::class.java
                                    ).start()
                                }
                            } else {
                                if (Constant.isDarkMode) {
                                    navigator.loadActivity(
                                        IsolatedFullActivity::class.java,
                                        ChangeThemeNightFragment::class.java
                                    ).start()
                                } else {
                                    navigator.loadActivity(
                                        IsolatedFullActivity::class.java,
                                        ChangeThemeDayFragment::class.java
                                    ).start()
                                }
                            }


                        }

                        // Delete Account
                        6 -> {
                            deleteAccount()
                        }

                        // Sign out
                        7 -> {
                            signOut()
                        }
                    }
                }
            }
        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeUnlinkDevice()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): SettingsFragmentBinding {
        return SettingsFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        if(appPreferences.getBoolean(Constant.APP_PREF_IS_SET_MESSAGE)){
            appPreferences.putBoolean(Constant.APP_PREF_IS_SET_MESSAGE, false)
            showMessage(getString(R.string.msg_notification_retention_setting_success))
        }
    }

    private fun toolbar() = with(binding) {
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_settings)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewSettings.adapter = settingsAdapter

        addData()
        settingsAdapter.notifyDataSetChanged()
    }

    private fun addData() = with(binding) {
        settingsAdapter.list.clear()
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_maintenance_mode,
                getString(R.string.title_maintenance_mode),
                getString(
                    R.string.label_maintenance_mode_settings_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_notification_priority,
                getString(R.string.title_notification_priority),
                getString(
                    R.string.label_notification_priority_settings_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_notification_retention,
                getString(R.string.title_notification_retention),
                getString(
                    R.string.label_control_notification_retention_settings
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_share_app, getString(R.string.title_share_app), getString(
                    R.string.label_share_app_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_privacy_policy,
                getString(R.string.title_terms_and_privacy_policy),
                getString(
                    R.string.label_terms_and_privacy_policy_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_change_themes,
                getString(R.string.title_change_your_theme),
                getString(
                    R.string.label_change_your_theme_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_delete_account,
                getString(R.string.title_delete_account_settings),
                getString(
                    R.string.label_delete_account_description
                )
            )
        )
        settingsAdapter.list.add(
            SettingsList(
                R.drawable.ic_settings_signout, getString(R.string.title_sign_out), getString(
                    R.string.label_sign_out_description
                )
            )
        )
    }

    private fun shareApp() {
        try {
            val appPackageName: String = requireActivity().packageName
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            val shareMessage =
                "${getString(R.string.app_name)} : \nhttps://play.google.com/store/apps/details?id=$appPackageName"

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    private fun deleteAccount() {
        customDialog =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setMessage("").create()
        val view = layoutInflater.inflate(R.layout.dialog_delete_account, null)
        val buttonDelete =
            view.findViewById<AppCompatButton>(R.id.buttonDelete)
        val buttonNo =
            view.findViewById<AppCompatButton>(R.id.buttonNo)

        buttonNo.setOnClickListener {
            customDialog.dismiss()
        }

        buttonDelete.setOnClickListener {
            appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, false)
            appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "")
            navigator.loadActivity(AuthActivity::class.java).byFinishingAll().start()
            /*val bundle = Bundle()
            bundle.putBoolean(Constants.BUNDLE_IS_DELETE_ACCOUNT, true)
            appPreferences.putBoolean(Constants.APP_PREFERENCES_IS_LOGIN, false)
            navigator.loadActivity(AuthActivity::class.java).addBundle(bundle)
                .byFinishingAll()
                .start()*/
//            callDeleteAccount()
            customDialog.dismiss()
        }
        customDialog.setView(view)
        customDialog.setCanceledOnTouchOutside(true)
        customDialog.show()
    }

    private fun signOut() {
        customDialog =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setMessage("").create()
        val view = layoutInflater.inflate(R.layout.dialog_sign_out, null)
        val buttonSignOut =
            view.findViewById<AppCompatButton>(R.id.buttonSignOut)
        val buttonNo =
            view.findViewById<AppCompatButton>(R.id.buttonNo)

        buttonNo.setOnClickListener {
            customDialog.dismiss()
        }

        buttonSignOut.setOnClickListener {
            callUnlinkDevice()
            /*val bundle = Bundle()
            bundle.putBoolean(Constants.BUNDLE_IS_DELETE_ACCOUNT, true)
            appPreferences.putBoolean(Constants.APP_PREFERENCES_IS_LOGIN, false)
            navigator.loadActivity(AuthActivity::class.java).addBundle(bundle)
                .byFinishingAll()
                .start()*/
//            callDeleteAccount()
            customDialog.dismiss()
        }
        customDialog.setView(view)
        customDialog.setCanceledOnTouchOutside(true)
        customDialog.show()
    }

    private fun callUnlinkDevice() {
        showLoader()
        apiViewModel.callUnlinkDevice()
    }

    private fun observeUnlinkDevice() {
        apiViewModel.unlinkDeviceLiveData.observe(this) {
            hideLoader()
            dbHelper.deleteAllFetchNotification()
            appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, false)
            appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "")
            navigator.loadActivity(AuthActivity::class.java).byFinishingAll().start()

        }
//            Log.e("link device", it.toString())
    }
}