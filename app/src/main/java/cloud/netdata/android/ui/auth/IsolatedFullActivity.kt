package cloud.netdata.android.ui.auth

import android.os.Bundle
import android.view.View
import cloud.netdata.android.R
import cloud.netdata.android.databinding.IsolatedAcitivtyFullBinding
import cloud.netdata.android.di.component.ActivityComponent
import cloud.netdata.android.ui.base.BaseActivity
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.manager.ActivityStarter

class IsolatedFullActivity : BaseActivity() {

    lateinit var isolatedFullActivityBinding: IsolatedAcitivtyFullBinding

    override fun findFragmentPlaceHolder(): Int {
        return R.id.placeHolder
    }

    override fun createViewBinding(): View {
        isolatedFullActivityBinding=IsolatedAcitivtyFullBinding.inflate(layoutInflater)
        return isolatedFullActivityBinding.root
    }


    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

            val page = intent.getSerializableExtra(ActivityStarter.ACTIVITY_FIRST_PAGE) as Class<BaseFragment<*>>
            load(page)
                    .setBundle(intent.extras!!)
                    .replace(false)
        }

    }
}