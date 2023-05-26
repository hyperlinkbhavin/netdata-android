package com.netdata.app.ui.auth.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.netdata.app.data.pojo.request.LoginRequest
import com.netdata.app.databinding.AuthFragmentLoginBinding
import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.exception.ApplicationException
import com.netdata.app.ui.auth.viewmodel.LoginViewModel
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.fragment.ChooseSpaceFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.Validator
import javax.inject.Inject

class LoginFragment : BaseFragment<AuthFragmentLoginBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory)[LoginViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): AuthFragmentLoginBinding {
        return AuthFragmentLoginBinding.inflate(inflater, container, attachToRoot)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun bindData() {
        manageClick()
    }

    private fun manageClick() = with(binding){
        buttonSignIn.setOnClickListener {
            if(validator()){
                appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, true)
                navigator.load(ChooseSpaceFragment::class.java).replace(false)
            }
        }

        imageViewClose.setOnClickListener {
            navigator.goBack()
        }
    }

    private fun validator(): Boolean = try {
        validator.submit(binding.editTextEmail)
            .checkEmpty().errorMessage("Please enter email address")
            .checkValidEmail().errorMessage("Please enter valid email")
            .check()
        true
    } catch (e: ApplicationException) {
        showMessage(e)
        false
    }

}