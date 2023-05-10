package com.netdata.app.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.R
import com.netdata.app.data.URLFactory
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
        toolbar()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_terms_and_privacy_policy)

        binding.webViewTermsAndConditions.loadUrl("https://www.google.com/intl/en_IN/policies/terms/archive/20070416/")
    }
}