package com.netdata.app.ui.auth.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.exception.CookiesHandlerError
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.customapi.DynamicViewModel

class WelcomeFragment: BaseFragment<AuthFragmentWelcomeBinding>() {

    private var recursiveCount = 0

    private val dynamicViewModel by lazy {
        ViewModelProvider(this)[DynamicViewModel::class.java]
    }

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeDynamicLink()
        observeGetSpaceList()
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): AuthFragmentWelcomeBinding {
        return AuthFragmentWelcomeBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        manageClick()

        val deeplink = arguments?.getString(Constant.BUNDLE_DEEPLINK)

        if(deeplink != ""){
            Log.e("link", deeplink!!)
            callDynamicLink(deeplink)
        }
    }

    private fun manageClick() = with(binding){
        buttonSignInWithEmailAddress.setOnClickListener {
            navigator.load(LoginFragment::class.java).replace(true)
        }

        buttonSignInWithQRCode.setOnClickListener {
            navigator.load(QRCodeLoginFragment::class.java).replace(true)
        }
    }

    private fun recursiveGetSpaceList(){
        if(Constant.COOKIE_SV.isNotEmpty()){
            Log.e("call", Constant.COOKIE_SV)
            callGetSpaceList()
        } else {
            if(recursiveCount < 5){
                Log.e("rec", recursiveCount.toString())
                recursiveCount++
                Handler(Looper.getMainLooper()).postDelayed({
                    recursiveGetSpaceList()
                },1000)
            }
        }
    }

    private fun callDynamicLink(link: String) {
//        Log.e("token", link)
        dynamicViewModel.callDynamicLink(link)
    }

    private fun observeDynamicLink() {
        dynamicViewModel.liveData.observe(this) {
            if (it is CookiesHandlerError) {
                if (it.map.isNotEmpty()) {
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SI, it.map["s_i"]!!)
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SI, it.map["s_v_${it.map["s_i"]}"]!!)
                    Constant.COOKIE_SI = it.map["s_i"]!!
                    Constant.COOKIE_SV = it.map["s_v_${it.map["s_i"]}"]!!
                    Log.e("cookie", Constant.COOKIE_SI)
                    callGetSpaceList()
                } else {
                    Log.e("else", "cookie")
                }
            } else {
                Log.e("else", "else")
            }
        }
    }

    private fun callGetSpaceList(){
        Log.e("spacecall", "spacecall")
        apiViewModel.callGetSpaceList()
    }

    private fun observeGetSpaceList(){
        apiViewModel.spaceListLiveData.observe(this){
            Log.e("space", it.toString())
        }
    }
}