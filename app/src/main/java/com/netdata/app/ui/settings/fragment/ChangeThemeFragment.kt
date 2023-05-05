package com.netdata.app.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import com.netdata.app.R
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChangeThemeFragmentBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.SettingsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment

class ChangeThemeFragment: BaseFragment<ChangeThemeFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ChangeThemeFragmentBinding {
        return ChangeThemeFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_change_theme)
    }

    private fun manageClick() = with(binding){
        constraintDayTheme.setOnClickListener {
            radioButtonDayTheme.isChecked = true
            radioButtonNightTheme.isChecked = false
        }

        constraintNightTheme.setOnClickListener {
            radioButtonDayTheme.isChecked = false
            radioButtonNightTheme.isChecked = true
        }

        radioButtonDayTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            radioButtonDayTheme.isChecked = isChecked
            radioButtonNightTheme.isChecked = !isChecked
        }

        radioButtonNightTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            radioButtonDayTheme.isChecked = !isChecked
            radioButtonNightTheme.isChecked = isChecked
        }

        buttonDone.setOnClickListener {
            navigator.goBack()
        }
    }
}