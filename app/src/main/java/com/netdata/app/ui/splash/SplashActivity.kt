package com.netdata.app.ui.splash

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.netdata.app.core.AppPreferences
import com.netdata.app.data.pojo.enumclass.ThemeMode
import com.netdata.app.databinding.SplashActivityBinding
import com.netdata.app.di.component.ActivityComponent
import com.netdata.app.ui.auth.AuthActivity
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseActivity
import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.ui.home.fragment.ChooseSpaceFragment
import com.netdata.app.utils.AppUtils
import com.netdata.app.utils.Constant
import com.netdata.app.utils.gone
import com.netdata.app.utils.visible
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    //Data store on after user login
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

        val data: Uri? = intent?.data

        if(data != null){
            val token = data.toString().substring(data.toString().lastIndexOf("upn=")+4)
            Log.e("deeplink", data.toString())
            Log.e("deeplink", data.toString().substring(data.toString().lastIndexOf("upn=")+4))
        }

        if (appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Night.name) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (appPreferences.getString(Constant.APP_PREF_DAY_NIGHT_MODE) == ThemeMode.Day.name) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
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
                        loadActivity(HomeActivity::class.java).byFinishingCurrent().start()
                    } else {
                        loadActivity(
                            IsolatedFullActivity::class.java,
                            ChooseSpaceFragment::class.java
                        ).byFinishingCurrent().start()
                    }
                } else {
                    loadActivity(AuthActivity::class.java).byFinishingCurrent().start()
                }
            }
        }, 3000)
    }

}