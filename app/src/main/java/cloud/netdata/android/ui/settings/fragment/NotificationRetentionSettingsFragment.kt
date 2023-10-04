package cloud.netdata.android.ui.settings.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.response.HomeNotificationList
import cloud.netdata.android.data.pojo.response.NotificationRetention
import cloud.netdata.android.databinding.NotificationRetentionSettingsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.settings.adapter.NotificationRetentionSettingsAdapter
import cloud.netdata.android.utils.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.notification_retention_settings_fragment.*


class NotificationRetentionSettingsFragment: BaseFragment<NotificationRetentionSettingsFragmentBinding>() {

    private val notificationRetentionSettingsAdapter by lazy {
        NotificationRetentionSettingsAdapter() { view->
            when (view.id) {
                R.id.radioButton -> {
                }
                R.id.constraintMain -> {
                }
            }
        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): NotificationRetentionSettingsFragmentBinding {
        return NotificationRetentionSettingsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        setAdapter()
        manageClick()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_notification_retention)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding){
        recyclerViewDeleteNotification.adapter = notificationRetentionSettingsAdapter

        val gson = Gson()
        val type = object : TypeToken<List<NotificationRetention>>() {}.type
        notificationRetentionSettingsAdapter.list.clear()
        notificationRetentionSettingsAdapter.list.addAll(gson.fromJson(appPreferences.getString(Constant.APP_PREF_NOTIFICATION_RETENTION), type))
        notificationRetentionSettingsAdapter.notifyDataSetChanged()
        Log.e("dd", notificationRetentionSettingsAdapter.list.toString())
    }

    private fun storeData(){
        Log.e("store", notificationRetentionSettingsAdapter.list.toString())
        appPreferences.putString(Constant.APP_PREF_NOTIFICATION_RETENTION, Gson().toJson(notificationRetentionSettingsAdapter.list))
    }

    private fun manageClick() = with(binding){
        buttonDone.setOnClickListener {
            storeData()
            appPreferences.putBoolean(Constant.APP_PREF_IS_SET_MESSAGE, true)
            navigator.goBack()
        }
    }
}