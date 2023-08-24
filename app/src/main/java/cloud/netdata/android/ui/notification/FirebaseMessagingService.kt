package cloud.netdata.android.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import cloud.netdata.android.R
import cloud.netdata.android.core.AppSession
import cloud.netdata.android.data.pojo.response.NotificationList
import cloud.netdata.android.ui.home.HomeActivity
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.localdb.DatabaseHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject


const val channelId = "notification_channel"
const val channelName = "com.hookd.ui.home.fragment"

class FirebaseMessagingService() : FirebaseMessagingService() {

    @Inject
    lateinit var appSession: AppSession
    lateinit var dbHelper: DatabaseHelper

    private var conversationTitle: String? = null

    companion object {
        const val DEFAULT_CHANNEL_ID = "7777"
        const val DEFAULT_CHANNEL_NAME = "Default"
        const val GROUP = "Group"
        private const val TAG = "NotificationService"
    }

    private lateinit var notificationMap: MutableMap<String, String>

    override fun onNewToken(token: String) {
        //cYiU8YMNSxmZNWEFe1Auel:APA91bEAqVmyQSrQT8cuqhfiQFb9ydZAInr7W8fmHSJ_itH8pQFiRqEobJ9IjygtRWBq1zXGpdTZ3_PyErlRtu4EAf1KIdYPprkG7mYPnSw_eqxRpLy_UDujLnWfpHhsKYbXrZcSZP5H
//        sendRegistrationToServer(token)
    }

    fun generateNotification(
        title: String,
        message: String,
        alertName: String
    ) {

// i have to add the if condition for user home and auth.
        val intent = Intent(this, HomeActivity::class.java)
        //this flag clear all the activity from the background and set this activity/fragment at the top.
        //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // this pending intent set that this flag can only be used for once.
        // val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val defaultSoundUri: Uri = Uri.parse(Constant.notificationPriorityList[1].soundUrl!!)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(Constant.PUSH_NOTIFICATION, alertName)
        intent.putExtra(Constant.NOTIFICATION_ICON, "message")

        //intent.putExtra(Constant.PUSH_NOTIFICATION,Gson().toJson(pushNotification))
        val notificationID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //createPendingIntentForHomeScreen(pushNotification)

        //@mipmap/ic_app_logo_round
        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setLights(Color.BLUE, 1, 1)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title, message))

        // now we have to create the notification manager

        val notificationManger =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManger.createNotificationChannel(notificationChannel)
        }

        notificationManger.notify(notificationID, builder.build())
    }

    /*fun generateNotification(title: String, message: String, notificationData: NotificationData) {
        val intent = Intent(this, HomeActivity::class.java)
        //this flag clear all the activity from the background and set this activity/fragment at the top.
        *//*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // this pending intent set that this flag can only be used for once.
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)*//*

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        Log.d("MyPushNotification", "createPendingIntentForHomeScreen: $notificationData")
        intent.putExtra(IntentKeys.PUSH_NOTIFICATION, notificationData)
        //intent.putExtra(Constant.PUSH_NOTIFICATION,Gson().toJson(pushnotification))
        val notificationID = System.currentTimeMillis().toInt()
        val pendingIntent =   PendingIntent.getActivity(
            this,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_logo_app)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        // now we have to create the notification manager

        val notificationManger =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManger.createNotificationChannel(notificationChannel)
        }
        notificationManger.notify(notificationID, builder.build())
        Log.e("generateNotification", "$title $message")
    }*/

    private fun getRemoteView(title: String, message: String): RemoteViews {

        val remoteView = RemoteViews("cloud.netdata.android", R.layout.push_notification)
        remoteView.setTextViewText(R.id.title_push, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.icon, R.drawable.ic_logo)

        return remoteView
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Message Data",remoteMessage.data["alert_name"].toString())
        generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!, remoteMessage.data["alert_name"].toString())

        Log.e("Message Title", remoteMessage.notification!!.title.toString())
        Log.e("Message Body", remoteMessage.notification!!.body.toString())
        Log.e("Message Id", remoteMessage.messageId.toString())
        Log.e("Message Type", remoteMessage.messageType.toString())
        Log.e("Message To", remoteMessage.to.toString())
        Log.e("Message CollapseKey", remoteMessage.collapseKey.toString())
        Log.e("Message From", remoteMessage.from.toString())
        Log.e("Message Notification", remoteMessage.notification.toString())
        Log.e("Message Priority", remoteMessage.priority.toString())
        Log.e("Message Original Priority", remoteMessage.originalPriority.toString())
        /*if (remoteMessage.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
            // twilio.conversations.new_message
            remoteMessage.data.let {
                notificationMap = it
                val notification = Notification()
                notification.receiverId = notificationMap["receiverId"]
                notification.receiverType = notificationMap["receiverType"]
                notification.title = notificationMap["title"]
                notification.senderId = notificationMap["senderId"]
                notification.senderType = notificationMap["senderType"]
                notification.message = notificationMap["message"]
                notification.createdAt = notificationMap["createdAt"]
                notification.notificationTag = notificationMap["tag"]
                Log.d("TAG", "Notification:  $it")

                val pushNotification = NotificationList.Results()
                pushNotification.tag = notificationMap["tag"]
                pushNotification.message = notificationMap["message"]
                pushNotification.title = notificationMap["title"]
                if (notificationMap["model_id"] != null) {
                    pushNotification.modelId =
                        notificationMap["model_id"].toString()
                }
                generateNotification(
                    notification.title!!,
                    notification.message!!,
                    pushNotification
                )
                Log.e("notificationTag", "onMessageReceived: $pushNotification")
            }
        }*/

       /* if (remoteMessage.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
//            Log.e("data", remoteMessage.data.toString())

            remoteMessage.data.let {
                notificationMap = it
                val notification = Notification()
                notification.receiver = notificationMap["receiver"]
                notification.tag = notificationMap["tag"]
                notification.title = notificationMap["title"]
                notification.message = notificationMap["message"]
                Log.d("TAG", "Notification:  $it")
//                generateNotification(notification.title!!, notification.message!!)

                //sendNotification("Sawem","5 min will start",null,null)

                val pushNotification = NotificationData()
                pushNotification.tag = notificationMap["tag"]
                pushNotification.message = notificationMap["message"]
                pushNotification.title = notificationMap["title"]
                if (notificationMap["receiver"] != null) {
                    pushNotification.receiver = notificationMap["receiver"].toString()
                }
                generateNotification(notification.title!!, notification.message!!, pushNotification)
            }
        }*/


//        generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        /*if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }*/

        /*if (message.notification!!.tag != null) {
            when (message.notification!!.tag) {
                "" -> {
                    //here we have to load the activity
                }
            }
        }*/
    }

    /*private fun sendRegistrationToServer(token: String) {
        if (::appSession.isInitialized)
            appSession.deviceId = token
    }*/

    // whenever app in killed state this intent will called.
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