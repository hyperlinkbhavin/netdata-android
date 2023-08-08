package com.netdata.app.utils.customapi

import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.data.pojo.response.DynamicLink
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.data.pojo.response.RoomList
import com.netdata.app.data.pojo.response.SpaceList
import retrofit2.Call
import retrofit2.http.*

interface MainApi {

    @POST("api/v2/auth/account/magic-link/mobile-app")
    fun loginData(@Body apiRequest: APIRequest): Call<Any>

    @Headers("Accept: application/json")
    @GET("ls/click")
    fun fetchData(@Query("upn") dynamicLink: String): Call<DynamicLink>

    @POST("api/v1/auth/account/mobile-app-token")
    fun linkDevice(@Header("Authorization") token: String, @Body apiRequest: APIRequest): Call<Any>

    @DELETE("api/v1/auth/account/mobile-app-token")
    fun unlinkDevice(@Header("Authorization") token: String): Call<Any>

    @GET("api/v3/spaces")
    fun getSpaceList(@Header("Authorization") token: String): Call<ArrayList<SpaceList>>

    @GET("api/v1/notifications/CloudMobileApp")
    fun fetchHomeNotification(@Header("Authorization") token: String): Call<ArrayList<HomeNotificationList>>

    @GET("api/v2/spaces/{spaceID}/rooms?show_all=true")
    fun getRoomsList(@Header("Authorization") token: String, @Path("spaceID") spaceID: String): Call<ArrayList<RoomList>>

    @GET("api/v2/spaces/{spaceID}/alarms")
    fun listSpaceAlertCount(@Header("Authorization") token: String, @Path("spaceID") spaceID: String): Call<Any>

    @GET("api/v2/spaces/{spaceID}/rooms/{roomID}/alarms-active")
    fun listActiveAlertPerRoom(
        @Header("Authorization") token: String,
        @Path("spaceID") spaceID: String,
        @Path("roomID") roomID: String
    ): Call<Any>

    @GET("api/v2/spaces/{spaceID}/rooms/{roomID}/alarms/{alarm}/nodes/{nodeID}/charts/{chart}")
    fun getAlertDetailed(
        @Header("Authorization") token: String,
        @Path("spaceID") spaceID: String,
        @Path("roomID") roomID: String,
        @Path("alarm") alarm: String,
        @Path("chart") chart: String
    ): Call<Any>

    @GET("api/v2/accounts/me/notifications/settings")
    fun getAccountNotificationsSettings(@Header("Authorization") token: String): Call<Any>

    @PATCH("api/v2/accounts/me/notifications/settings")
    fun updateAccountNotificationsSettings(@Header("Authorization") token: String, @Body apiRequest: APIRequest): Call<Any>

    @POST("api/v2/spaces/{spaceID}/notifications/silencing/rule")
    fun silenceSpace(@Header("Authorization") token: String, @Path("spaceID") spaceID: String, @Body apiRequest: APIRequest): Call<Any>

    @POST("api/v2/spaces/{spaceID}/notifications/silencing/rules/delete")
    fun unsilenceSpace(@Header("Authorization") token: String, @Path("spaceID") spaceID: String, @Body apiRequest: APIRequest): Call<Any>

}