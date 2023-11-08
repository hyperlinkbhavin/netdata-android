package cloud.netdata.android.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import cloud.netdata.android.R
import cloud.netdata.android.databinding.TermsAndConditionsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment

class TermsAndConditionsFragment: BaseFragment<TermsAndConditionsFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): cloud.netdata.android.databinding.TermsAndConditionsFragmentBinding {
        return TermsAndConditionsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_terms_and_privacy_policy)

        binding.webViewTermsAndConditions.loadUrl("https://www.netdata.cloud/terms/?_gl=1*1psr83l*_ga*ODY0NDQzNDMxLjE2OTEwNzQ4MDY.*_ga_J69Z2JCTFB*MTY5OTQ0NjU0NC4zMDAuMS4xNjk5NDQ2NTU5LjQ1LjAuMA..")
    }
}