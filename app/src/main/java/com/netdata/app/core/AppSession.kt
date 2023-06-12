package com.netdata.app.core

import android.content.Context
import android.provider.Settings
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.netdata.app.data.pojo.User
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AppSession @Inject
constructor(private val appPreferences: AppPreferences,
            private val context: Context,
            @param:Named("api-key") override var apiKey: String) : Session {

    private val gson: Gson = Gson()

    override var user: User? = null
        get() {
            if (field == null) {
                val userJSON = appPreferences.getString(USER_JSON)
                field = gson.fromJson(userJSON, User::class.java)
            }
            return field
        }
        set(value) {
            field = value
            val userJson = gson.toJson(value)
            if (userJson != null)
                appPreferences.putString(USER_JSON, userJson)
        }


    override var userSession: String
        get() = appPreferences.getString(Session.USER_SESSION)
        set(userSession) = appPreferences.putString(Session.USER_SESSION, userSession)


    override var userId: String
        get() = appPreferences.getString(Session.USER_ID)
        set(userId) = appPreferences.putString(Session.USER_ID, userId)


    /*override*//* open below comment after Firebase integration *//*//token = FirebaseInstanceId.getInstance().getToken();
    val deviceId: String
        get() {
            var token = ""
            if (token.isEmpty())
                token = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            return token
        }*/

    override var deviceId: String
        /*get() = appPreferences.getString(Session.DEVICE_ID)*/
        get() {
            /*var token = ""
            if (token.isEmpty())
                token = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return token*/
            return if (appPreferences.getString(Session.DEVICE_ID).isNullOrBlank().not())
                appPreferences.getString(Session.DEVICE_ID)
            else Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
        set(deviceId) = appPreferences.putString(Session.DEVICE_ID, deviceId)

    override fun getFirebaseDeviceId(callback: (deviceID: String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deviceId = task.result
                callback.invoke(task.result)
            } else {
                callback.invoke("")
            }
        }
    }

    override//  return StringUtils.equalsIgnoreCase(appPreferences.getString(Common.LANGUAGE), "ar") ? LANGUAGE_ARABIC : LANGUAGE_ENGLISH;
    val language: String
        get() = "en"


    override fun clearSession() {
        appPreferences.clearAll()
    }


    companion object {
        const val USER_JSON = "user_json"
    }


}
