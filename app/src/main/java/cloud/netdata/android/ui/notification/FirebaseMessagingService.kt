package cloud.netdata.android.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import cloud.netdata.android.R
import cloud.netdata.android.core.AppSession
import cloud.netdata.android.ui.home.HomeActivity
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.localdb.DatabaseHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject


const val channelId = "notification_channel"
const val channelName = "cloud.netdata.android.ui.home.fragment"

class FirebaseMessagingService() : FirebaseMessagingService() {

    @Inject
    lateinit var appSession: AppSession
    lateinit var dbHelper: DatabaseHelper

    private var conversationTitle: String? = null
    private var isVibrate = false
    private var isBanner = false
    private var isSound = false
    lateinit var builder: NotificationCompat.Builder

    companion object {
        const val DEFAULT_CHANNEL_ID = "7777"
        const val DEFAULT_CHANNEL_NAME = "Default"
        const val GROUP = "Group"
        private const val TAG = "NotificationService"
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize your database helper here
        dbHelper = DatabaseHelper(this) // Replace with your DatabaseHelper initialization
    }

    private lateinit var notificationMap: MutableMap<String, String>

    override fun onNewToken(token: String) {
        //cYiU8YMNSxmZNWEFe1Auel:APA91bEAqVmyQSrQT8cuqhfiQFb9ydZAInr7W8fmHSJ_itH8pQFiRqEobJ9IjygtRWBq1zXGpdTZ3_PyErlRtu4EAf1KIdYPprkG7mYPnSw_eqxRpLy_UDujLnWfpHhsKYbXrZcSZP5H
//        sendRegistrationToServer(token)
    }

    fun generateNotification(
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, HomeActivity::class.java)

        var defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val defaultSoundUri: Any

        try {
            val notificationList = dbHelper.getAllDataFromFetchNotification(isSimpleData = true)
            Log.e("notificationList", notificationList.toString())
            val notificationPriorityData = dbHelper.getAllDataFromNotificationPriority()
            Log.e("prioData", notificationPriorityData.toString())
            val alertData = notificationList.find {
                it.data!!.host[0].id == data["node_id"] && it.data!!.netdata!!.space!!.id == data["space_id"]
                        && it.data!!.netdata!!.alert!!.name[0].equals(
                    data["alert_name"],
                    ignoreCase = true
                )
            }
            Log.e("alertData", alertData.toString())

            if(alertData != null){
                val notificationPriority =
                    notificationPriorityData.find { it.priority.equals(alertData!!.priority, true) }
                Log.e("PRIORITY", notificationPriority.toString())

                isVibrate = notificationPriority!!.isVibration == 1
                isBanner = notificationPriority.isBanner == 1
                isSound = notificationPriority.isSound == 1
            }

            /*defaultSoundUri = if(!notificationPriority.soundUrl.isNullOrEmpty()){
            Uri.parse(notificationPriority.soundUrl)
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }*/

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(Constant.PUSH_NOTIFICATION, data["alert_name"])
            intent.putExtra(Constant.NOTIFICATION_ICON, "message")

            val notificationID = System.currentTimeMillis().toInt()
            val pendingIntent = PendingIntent.getActivity(
                this,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            //@mipmap/ic_app_logo_round
            builder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(
                    setBigContentView(title, message)
                ) // set more view content when SwipeDown (pull down)

            if(isBanner){
                Log.e("isBanner", isBanner.toString())
                builder.priority = NotificationCompat.PRIORITY_HIGH
            } else {
                builder.priority = NotificationCompat.PRIORITY_LOW
            }
            if (isSound) {
                Log.e("isSound", isSound.toString())
                builder.setSound(defaultSoundUri)
            } else {
                builder.setSilent(true)
            }
            if (isVibrate) {
                Log.e("isVibrate",isVibrate.toString())
                builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            } else {
                builder.setVibrate(longArrayOf(0, 0, 0, 0))
            }
            builder = builder.setContent(getRemoteView(title, message))

            val notificationManger =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManger.createNotificationChannel(notificationChannel)
            }

            Constant.MY_NOTIFICATION_MESSAGE = data["space_name"]!!.ifEmpty {
                "Space"
            }
            val newIntent = Intent(Constant.MY_NOTIFICATION_ACTION)

            sendBroadcast(newIntent)

            notificationManger.notify(notificationID, builder.build())
        } catch (e: Exception) {
            Log.e("notification crash", e.toString())
        }

        /*intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(Constant.PUSH_NOTIFICATION, data["alert_name"])
        intent.putExtra(Constant.NOTIFICATION_ICON, "message")

        val notificationID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setLights(Color.BLUE, 1, 1)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManger =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManger.createNotificationChannel(notificationChannel)
        }

        Constant.MY_NOTIFICATION_MESSAGE = data["space_name"]!!.ifEmpty {
            "Space"
        }
        val newIntent = Intent(Constant.MY_NOTIFICATION_ACTION)
        sendBroadcast(newIntent)
        notificationManger.notify(notificationID, builder.build())*/
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {

        val remoteView = RemoteViews("cloud.netdata.android", R.layout.push_notification)
        remoteView.setTextViewText(R.id.title_push, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.icon, R.drawable.ic_logo)

        return remoteView
    }


    @SuppressLint("RemoteViewLayout")
    private fun setBigContentView(title: String, message: String): RemoteViews {
//        val videoUri = Uri.parse(img)
//        val thumbnail = extractVideoThumbnail(videoUri)

        /*val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isNightModeEnabled = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES

        val textColor = if (isNightModeEnabled) {
            // Use a light color for dark mode
            ContextCompat.getColor(applicationContext, R.color.colorWhite)
        } else {
            // Use a dark color for light mode
            ContextCompat.getColor(applicationContext, R.color.colorBlack1C)
        }*/
        val remoteView = RemoteViews("cloud.netdata.android", R.layout.long_push_notification)
        remoteView.setTextViewText(R.id.textViewLongTitle, title)
//        remoteView.setTextColor(R.id.textViewLongTitle, textColor)

        remoteView.setTextViewText(R.id.textViewLongMessage, message)
//        remoteView.setTextColor(R.id.textViewLongMessage, textColor)

//        remoteView.setImageViewBitmap(R.id.imageViewVideoThumbnail, thumbnail)
        remoteView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher)
        return remoteView
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Message Data", remoteMessage.data["alert_name"].toString())
        generateNotification(
            remoteMessage.notification!!.title!!,
            remoteMessage.notification!!.body!!,
            remoteMessage.data
        )

        /*Log.e("Message Title", remoteMessage.notification!!.title.toString())
        Log.e("Message Body", remoteMessage.notification!!.body.toString())
        Log.e("Message Id", remoteMessage.messageId.toString())
        Log.e("Message Type", remoteMessage.messageType.toString())
        Log.e("Message To", remoteMessage.to.toString())
        Log.e("Message CollapseKey", remoteMessage.collapseKey.toString())
        Log.e("Message From", remoteMessage.from.toString())
        Log.e("Message Notification", remoteMessage.notification.toString())
        Log.e("Message Priority", remoteMessage.priority.toString())
        Log.e("Message Original Priority", remoteMessage.originalPriority.toString())*/

    }

    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("MyFirebaseMessagingService")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())

            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

}