package com.netdata.app.ui.auth.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.exception.CookiesHandlerError
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.fragment.ChooseSpaceFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.customapi.DynamicViewModel
import com.netdata.app.utils.localdb.DatabaseHelper

class WelcomeFragment: BaseFragment<AuthFragmentWelcomeBinding>() {

    lateinit var dbHelper: DatabaseHelper

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
        observeLinkDevice()
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): AuthFragmentWelcomeBinding {
        return AuthFragmentWelcomeBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        manageClick()

        val deeplink = arguments?.getString(Constant.BUNDLE_DEEPLINK)

        if(!deeplink.isNullOrEmpty()){
            Log.e("link", deeplink!!)
            callDynamicLink(deeplink)
        } /*else {
            val table1DataList = dbHelper.getAllDataFromSpace()
            for (data in table1DataList) {
                Log.e("tag","Name: ${data.name}, Slug: ${data.slug}, \nPermissions: ${data.permissions},\nPlan: ${data.planDefinition}")
            }
        }*/



        // Insert data into Table 1
        /*dbHelper.insertDataIntoTable1("John Doe", "john@example.com")

// Insert data into Table 2
        dbHelper.insertDataIntoTable2("Title", "Content")*/

// Retrieve data from Table 1

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
        Log.e("token", link)
        showLoader()
        dynamicViewModel.callDynamicLink(link)
    }

    private fun observeDynamicLink() {
        dynamicViewModel.liveData.observe(this) {
            hideLoader()
            if(it.responseCode == 200 && it.data!!.token!!.isNotEmpty()){
                session.userSession = it.data.token!!
                Constant.TOKEN = session.userSession
                session.getFirebaseDeviceId { deviceId ->
                    session.deviceId = deviceId
                    callLinkDevice()
                }
            } else {
                showMessage("Link expire! Try again")
            }
        }
    }

    private fun callLinkDevice(){
        showLoader()
        apiViewModel.callLinkDevice(APIRequest( token = session.deviceId))
    }

    private fun observeLinkDevice(){
        apiViewModel.linkDeviceLiveData.observe(this){
            hideLoader()
            if(!it.isError){
                if(it.responseCode == 200){
                    appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, true)
                    navigator.load(ChooseSpaceFragment::class.java).replace(false)
                } else {
                    showMessage("Session expire! Try again")
                }
            } else {
                Log.e("failure link", "fail")
            }
        }
    }
}