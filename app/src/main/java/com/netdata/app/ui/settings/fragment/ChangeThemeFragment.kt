package com.netdata.app.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
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

    }
}