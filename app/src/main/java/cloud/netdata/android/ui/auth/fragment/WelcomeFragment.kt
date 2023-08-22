package cloud.netdata.android.ui.auth.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.databinding.AuthFragmentWelcomeBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.exception.CookiesHandlerError
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.home.fragment.ChooseSpaceFragment
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.customapi.DynamicViewModel
import cloud.netdata.android.utils.localdb.DatabaseHelper

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
        showLoader()
        dynamicViewModel.callDynamicLink(link)
    }

    private fun observeDynamicLink() {
        dynamicViewModel.liveData.observe(this) {
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoader()
                if(Constant.dynamicResponseUrl.contains("app.netdata.cloud/api/v2/auth/account/magic-link/mobile-app/login")){
                    if(it.responseCode == 200 && it.data!!.token!!.isNotEmpty()){
                        session.userSession = "bearer ${it.data.token!!}"
                        Constant.TOKEN = session.userSession
                        session.getFirebaseDeviceId { deviceId ->
                            session.deviceId = deviceId
                            callLinkDevice()
                        }
                    } else {
                        showMessage("Link expire! Try again")
                    }
                } else {
                    showMessage("Please login to view alert")
                }

            },1000)

            /*if (it is CookiesHandlerError) {
                if (it.map.isNotEmpty()) {
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SI, it.map["s_i"]!!)
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SV, it.map["s_v_${it.map["s_i"]}"]!!)
                    Constant.COOKIE_SI = it.map["s_i"]!!
                    Constant.COOKIE_SV = it.map["s_v_${it.map["s_i"]}"]!!
                    callLinkDevice()
                    Log.e("cookie", Constant.COOKIE_SI)
                } else {
                    Log.e("else", "cookie")
                }
            } else {
                Log.e("else", "else")
            }*/
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