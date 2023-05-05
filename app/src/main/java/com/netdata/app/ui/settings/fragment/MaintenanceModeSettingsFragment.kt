package com.netdata.app.ui.settings.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.R
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.MaintenanceModeSettingsFragmentBinding
import com.netdata.app.databinding.SettingsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.settings.adapter.MaintenanceModeSettingsAdapter

class MaintenanceModeSettingsFragment: BaseFragment<MaintenanceModeSettingsFragmentBinding>() {

    private val maintenanceModeSettingsAdapter by lazy {
        MaintenanceModeSettingsAdapter(){ view, position, item ->
            when(view.id){
                R.id.constraintDisableNotifications -> {
                    getCheckData()
                }
            }
        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): MaintenanceModeSettingsFragmentBinding {
        return MaintenanceModeSettingsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
        setAdapter()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_maintenance_mode_settings)
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private fun manageClick() = with(binding){
        switchDisableAllNotifications.setOnClickListener {
            Log.e("all", switchDisableAllNotifications.isChecked.toString())
            for(item in maintenanceModeSettingsAdapter.list){
                item.isSelected = switchDisableAllNotifications.isChecked
            }
            maintenanceModeSettingsAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding){
        recyclerViewMaintenanceMode.adapter = maintenanceModeSettingsAdapter

        addData()
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    private fun addData(){
        maintenanceModeSettingsAdapter.list.add(WarRoomsList("Space 1"))
        maintenanceModeSettingsAdapter.list.add(WarRoomsList("Space 2"))
        maintenanceModeSettingsAdapter.list.add(WarRoomsList("Space 3"))
        maintenanceModeSettingsAdapter.list.add(WarRoomsList("Space 4"))
    }

    private fun getCheckData(){
        val data = maintenanceModeSettingsAdapter.list.find { !it.isSelected }
        binding.switchDisableAllNotifications.isChecked = data?.name.isNullOrEmpty()
    }
}