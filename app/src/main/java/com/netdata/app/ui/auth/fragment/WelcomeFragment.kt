package com.netdata.app.ui.auth.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment

class WelcomeFragment: BaseFragment<AuthFragmentWelcomeBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): AuthFragmentWelcomeBinding {
        return AuthFragmentWelcomeBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        manageClick()
    }

    private fun manageClick() = with(binding){
        buttonSignInWithEmailAddress.setOnClickListener {
            navigator.load(LoginFragment::class.java).replace(true)
        }

        buttonSignInWithQRCode.setOnClickListener {
            navigator.load(QRCodeLoginFragment::class.java).replace(true)
        }
    }
}