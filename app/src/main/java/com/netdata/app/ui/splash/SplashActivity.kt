package com.netdata.app.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.netdata.app.databinding.SplashActivityBinding
import com.netdata.app.di.component.ActivityComponent
import com.netdata.app.ui.auth.AuthActivity
import com.netdata.app.ui.base.BaseActivity

class SplashActivity : BaseActivity() {
    //Data store on after user login
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
        Handler(Looper.getMainLooper()).postDelayed({
            loadActivity(AuthActivity::class.java).byFinishingAll().start()
        }, 2000)
    }

}