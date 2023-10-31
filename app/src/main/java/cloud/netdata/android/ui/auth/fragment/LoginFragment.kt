package cloud.netdata.android.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.R
import cloud.netdata.android.data.URLFactory
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.databinding.AuthFragmentLoginBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.exception.ApplicationException
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.getVal

class LoginFragment : BaseFragment<AuthFragmentLoginBinding>() {

    /*private val loginViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]
    }*/

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
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
        observeMagicLink()
    }

    override fun bindData() {
        manageClick()
    }

    private fun manageClick() = with(binding) {
        buttonSignIn.setOnClickListener {
            if (validator()) {
                session.getFirebaseDeviceId { deviceId ->
                    session.deviceId = deviceId
                    callMagicLink()
                    /*appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, true)
                    navigator.load(ChooseSpaceFragment::class.java).replace(false)*/
                }
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

    private fun callMagicLink() {
        showLoader()
        apiViewModel.callMagicLink(
            APIRequest(
                email = binding.editTextEmail.getVal(),
                /*redirectURI = URLFactory.Link.SIGN_IN_LINK,
                registerURI = URLFactory.Link.SIGN_UP_LINK*/
            )
        )
    }

    private fun observeMagicLink() {
        /*loginViewModel.magicLinkLiveData.observe(this, { responseBody ->
            Log.e("success", "success")
        }, { _ ->
            true
        })*/

        apiViewModel.magicLinkLiveData.observe(this) {
            hideLoader()
            if(!it.isError){
                showMessage(getString(R.string.label_link_sent_on_email))
            }
        }
    }

}