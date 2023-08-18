package cloud.netdata.android.utils.customapi

import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.data.pojo.response.*
import retrofit2.Call
import retrofit2.http.*

interface MainApi {

    @POST("api/v2/auth/account/magic-link")
//    @POST("api/v2/auth/account/magic-link/mobile-app")
    fun loginData(@Body apiRequest: APIRequest): Call<Any>

    @Headers("Accept: application/json")
    @GET("ls/click")
    fun fetchData(@Query("upn") dynamicLink: String): Call<DynamicLink>

    @POST("api/v1/auth/account/mobile-app-token")
    fun linkDevice(@Header("Cookie") token: String, @Body apiRequest: APIRequest): Call<Any>

    @DELETE("api/v1/auth/account/mobile-app-token")
    fun unlinkDevice(@Header("Cookie") token: String): Call<Any>

    @GET("api/v3/spaces")
    fun getSpaceList(@Header("Cookie") token: String): Call<ArrayList<SpaceList>>

    @GET("api/v1/notifications/CloudMobileApp")
    fun fetchHomeNotification(@Header("Cookie") token: String): Call<ArrayList<HomeNotificationList>>

    @GET("api/v2/spaces/{spaceID}/rooms?show_all=true")
    fun getRoomsList(@Header("Cookie") token: String, @Path("spaceID") spaceID: String): Call<ArrayList<RoomList>>

    @GET("api/v2/spaces/{spaceID}/alarms")
    fun listSpaceAlertCount(@Header("Cookie") token: String, @Path("spaceID") spaceID: String): Call<Any>

    @GET("api/v2/spaces/{spaceID}/rooms/{roomID}/alarms-active")
    fun listActiveAlertPerRoom(
        @Header("Cookie") token: String,
        @Path("spaceID") spaceID: String,
        @Path("roomID") roomID: String
    ): Call<Any>

    @GET("api/v2/spaces/{spaceID}/rooms/{roomID}/alarms/{alarm}/nodes/{nodeID}/charts/{chart}")
    fun getAlertDetailed(
        @Header("Cookie") token: String,
        @Path("spaceID") spaceID: String,
        @Path("roomID") roomID: String,
        @Path("alarm") alarm: String,
        @Path("chart") chart: String
    ): Call<Any>

    @GET("api/v2/accounts/me/notifications/settings")
    fun getAccountNotificationsSettings(@Header("Cookie") token: String): Call<Any>

    @PATCH("api/v2/accounts/me/notifications/settings")
    fun updateAccountNotificationsSettings(@Header("Cookie") token: String, @Body apiRequest: APIRequest): Call<Any>

    @POST("api/v2/spaces/{spaceID}/notifications/silencing/rule")
    fun silenceSpace(@Header("Cookie") token: String, @Path("spaceID") spaceID: String, @Body apiRequest: APIRequest): Call<SilenceRule>

    @POST("api/v2/spaces/{spaceID}/notifications/silencing/rules/delete")
    fun unsilenceSpace(@Header("Cookie") token: String, @Path("spaceID") spaceID: String, @Body params: ArrayList<String>): Call<Any>

}