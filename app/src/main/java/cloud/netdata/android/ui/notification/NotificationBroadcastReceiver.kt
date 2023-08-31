package cloud.netdata.android.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cloud.netdata.android.ui.home.fragment.HomeFragment
import cloud.netdata.android.utils.Constant

class NotificationBroadcastReceiver(private val homeFragment: Fragment) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent?.action == Constant.MY_NOTIFICATION_ACTION) {
//                val message = intent.getStringExtra(Constant.MY_NOTIFICATION_MESSAGE)
                if (homeFragment is HomeFragment) {
                    homeFragment.showNotificationSnackbar()
                }
            }
        } catch (e: Exception) {

        }
    }
}