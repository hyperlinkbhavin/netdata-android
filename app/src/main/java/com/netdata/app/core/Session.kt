package com.netdata.app.core

import com.netdata.app.data.pojo.User

public interface Session {

    var apiKey: String

    var userSession: String

    var userId: String

    var deviceId: String

    var user: User?

    val language: String

    fun getFirebaseDeviceId(callback: (deviceID: String) -> Unit)

    fun clearSession()

    companion object {
        const val API_KEY = "api-key"
        const val USER_SESSION = "token"
        const val USER_ID = "USER_ID"
        const val DEVICE_TYPE = "A"
        const val DEVICE_ID = "DEVICE_ID"
        const val LANGUAGE = "accept-language"
    }
}
