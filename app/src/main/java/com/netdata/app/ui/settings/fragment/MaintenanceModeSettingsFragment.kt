package com.netdata.app.ui.settings.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import com.netdata.app.R
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.MaintenanceModeSettingsFragmentBinding
import com.netdata.app.databinding.SettingsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.settings.adapter.MaintenanceModeSettingsAdapter
import com.netdata.app.utils.gone
import com.netdata.app.utils.visible
import java.util.*

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
            if (switchDisableAllNotifications.isChecked){
                radioGroupAllNotifications.visible()
            }else{
                radioGroupAllNotifications.gone()
            }
            maintenanceModeSettingsAdapter.notifyDataSetChanged()
        }
        textViewUntilDate.setOnClickListener {
            datePicker()
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
    fun datePicker()=with(binding){
        val mCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, date ->
                val date = date.toString()+"/"+month.toString()+"/"+year.toString().drop(2)
                timePicker(date)
            },
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DATE)
        )
        datePickerDialog.datePicker.minDate = mCalendar.time.time
        datePickerDialog.show()
    }

    fun timePicker(date: String) =with(binding){
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog( requireContext(),
            { timePicker, selectedHour, selectedMinute ->
                textViewUntilDate.setText(date+", "+selectedHour.toString()+":"+selectedMinute.toString() )
            },
            hour,
            minute,
            false) //Yes 24 hour time

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
}