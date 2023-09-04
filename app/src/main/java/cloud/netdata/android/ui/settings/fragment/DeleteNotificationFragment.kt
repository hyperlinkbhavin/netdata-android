package cloud.netdata.android.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import cloud.netdata.android.R
import cloud.netdata.android.databinding.DeleteNotificationFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment


class DeleteNotificationFragment: BaseFragment<DeleteNotificationFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): DeleteNotificationFragmentBinding {
        return DeleteNotificationFragmentBinding.inflate(inflater,container,attachToRoot)
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
        }

        constraintNightTheme.setOnClickListener {
        }

        radioButtonDayTheme.setOnCheckedChangeListener { buttonView, isChecked ->
        }

        radioButtonNightTheme.setOnCheckedChangeListener { buttonView, isChecked ->
        }

        buttonDone.setOnClickListener {
            navigator.goBack()
        }
    }
}