package cloud.netdata.android.ui.splash

import android.app.UiModeManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.core.AppPreferences
import cloud.netdata.android.data.pojo.enumclass.ThemeMode
import cloud.netdata.android.databinding.SplashActivityBinding
import cloud.netdata.android.di.component.ActivityComponent
import cloud.netdata.android.ui.auth.AuthActivity
import cloud.netdata.android.ui.auth.IsolatedFullActivity
import cloud.netdata.android.ui.base.BaseActivity
import cloud.netdata.android.ui.home.HomeActivity
import cloud.netdata.android.ui.home.fragment.ChooseSpaceFragment
import cloud.netdata.android.utils.AppUtils
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.customapi.DynamicViewModel
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.visible
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.content.Intent

class SplashActivity : BaseActivity() {

    //Data store on after user login
    private val dynamicViewModel by lazy {
        ViewModelProvider(this)[DynamicViewModel::class.java]
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    lateinit var splashActivityBinding: SplashActivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        splashActivityBinding = SplashActivityBinding.inflate(layoutInflater)
        return splashActivityBinding.root
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        observeDynamicLink()

        Constant.COOKIE_SI = appPreferences.getString(Constant.APP_PREF_COOKIE_SI)
        Constant.COOKIE_SV = appPreferences.getString(Constant.APP_PREF_COOKIE_SV)
        Constant.TOKEN = session.userSession

//        val data: Uri? = intent?.data
        var url = ""
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data

        if (appLinkData != null) {
            Log.e("deeplink", appLinkData.toString())
            if (appLinkData.toString().contains("/spaces/")) {
//                url = appLinkData.toString().split("=")[1]
                url = appLinkData.toString()
            } else {
                val tokenPattern = "token=([^&]*)".toRegex()
                val matchResult = tokenPattern.find(appLinkData.toString())
                url = matchResult?.groupValues?.getOrNull(1)!!
                Log.e("token", url)
            }
        }

        if (appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Night.name) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Constant.isDarkMode = true
        } else if (appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Day.name) {
//        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Constant.isDarkMode = false
        } else {
            val uiModeManager =
                getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            Constant.isDarkMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler(Looper.getMainLooper()).postDelayed({
            splashActivityBinding.apply {
                imageViewLogo.gone()
                imageViewNetdata.visible()
            }

            splashActivityBinding.apply {
                imageViewNetdata.animate().apply {
                    scaleY(1.0F).duration = 1500
                    scaleX(1.0F).duration = 1500
                }
            }
        }, 1500)

        // Zoom out animation
        /*Handler(Looper.getMainLooper()).postDelayed({

        }, 1500)*/

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss.SSS")
        val currentDate = sdf.format(Date())

        Handler(Looper.getMainLooper()).postDelayed({
            if (!AppUtils.isOpenRecently()) {
                if (appPreferences.getBoolean(Constant.APP_PREF_IS_LOGIN)) {
                    if (!appPreferences.getString(Constant.APP_PREF_SPACE_NAME).isNullOrEmpty()) {
                        loadActivity(HomeActivity::class.java).addBundle(bundleOf(Constant.BUNDLE_DEEPLINK to url)).byFinishingCurrent().start()
                    } else {
                        loadActivity(
                            IsolatedFullActivity::class.java,
                            ChooseSpaceFragment::class.java
                        ).byFinishingCurrent().start()
                    }
                } else {
                    loadActivity(AuthActivity::class.java).addBundle(bundleOf(Constant.BUNDLE_DEEPLINK to url)).byFinishingCurrent().start()
                }
            }
        }, 3000)
    }

    private fun getCookie(siteName: String?): String? {
        val cookieValue: String? = null
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        if (cookies != null) {
            val temp = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (ar1 in temp) {
                Log.e("cookies", ar1)
            }
        }
        return cookieValue
    }

    fun getCookiesFromUrl(urlString: String): List<String> {
        val cookies = mutableListOf<String>()

        val thread = Thread {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            // Make a request to the URL
            connection.connect()

            // Retrieve the cookies from the response headers
            val cookieHeaders = connection.headerFields["Set-Cookie"]
            if (cookieHeaders != null) {
                Log.e("set", cookieHeaders.toString())
                for (header in cookieHeaders) {
                    // Extract the cookie value from the header
                    val cookieValue = header.split(";")[0]
                    cookies.add(cookieValue)
                }
            }

            // Close the connection
            connection.disconnect()
        }

        thread.start()
        thread.join()

        return cookies
    }

    /*private fun callDynamicLink(link: String) {
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
                } else {
                    Log.e("else", "cookie")
                }
            } else {
                Log.e("else", "else")
            }
        }
    }*/

}