package com.netdata.app.ui.settings.fragment

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.netdata.app.R
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.NotificationPrioritySettingsFragmentBinding
import com.netdata.app.databinding.SettingsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment

class NotificationPrioritySettingsFragment: BaseFragment<NotificationPrioritySettingsFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): NotificationPrioritySettingsFragmentBinding {
        return NotificationPrioritySettingsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_notification_priority_settings)

        checkSwitch(switchHighPrioritySound, buttonHighPriorityApplyCustomTune)
        checkSwitch(switchMediumPrioritySound, buttonMediumPriorityApplyCustomTune)
        checkSwitch(switchLowPrioritySound, buttonLowPriorityApplyCustomTune)
    }

    private fun manageClick() = with(binding){
        switchHighPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonHighPriorityApplyCustomTune)
        }

        switchMediumPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonMediumPriorityApplyCustomTune)
        }

        switchLowPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonLowPriorityApplyCustomTune)
        }

        buttonHighPriorityApplyCustomTune.setOnClickListener {
            Log.e("click", "click")
        }

        buttonMediumPriorityApplyCustomTune.setOnClickListener {

        }

        buttonLowPriorityApplyCustomTune.setOnClickListener {

        }
    }

    private fun checkSwitch(switchView: SwitchCompat, buttonView: AppCompatButton){
        if(switchView.isChecked){
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF))
        }
    }

    private fun switchCheckChanged(isChecked: Boolean, buttonView: AppCompatButton){
        if(isChecked){
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF))
        }
    }
}