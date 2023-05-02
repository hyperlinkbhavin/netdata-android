package com.netdata.app.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.databinding.TermsAndConditionsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment

class TermsAndConditionsFragment: BaseFragment<TermsAndConditionsFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): TermsAndConditionsFragmentBinding {
        return TermsAndConditionsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {

    }
}