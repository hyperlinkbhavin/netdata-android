package com.netdata.app.ui.home

import android.os.Bundle
import android.view.View
import com.netdata.app.R
import com.netdata.app.databinding.HomeActivityBinding
import com.netdata.app.di.component.ActivityComponent
import com.netdata.app.ui.base.BaseActivity
import com.netdata.app.ui.home.fragment.HomeFragment

class HomeActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: HomeActivityBinding

    override fun createViewBinding(): View {
        binding = HomeActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun findFragmentPlaceHolder(): Int = R.id.placeHolder

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load(HomeFragment::class.java).replace(false)

    }

    override fun onClick(v: View) {

    }


}
