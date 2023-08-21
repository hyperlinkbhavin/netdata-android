package cloud.netdata.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import cloud.netdata.android.R
import cloud.netdata.android.databinding.HomeActivityBinding
import cloud.netdata.android.di.component.ActivityComponent
import cloud.netdata.android.ui.base.BaseActivity
import cloud.netdata.android.ui.home.fragment.HomeFragment
import cloud.netdata.android.utils.Constant

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
        load(HomeFragment::class.java).setBundle(
            bundleOf(
                Constant.BUNDLE_DEEPLINK to intent.getStringExtra(
                    Constant.BUNDLE_DEEPLINK))
        ).replace(false)

    }

    override fun onClick(v: View) {

    }


}
