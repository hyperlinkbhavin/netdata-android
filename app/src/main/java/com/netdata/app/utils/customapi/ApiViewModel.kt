package com.netdata.app.utils.customapi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netdata.app.data.pojo.MyResponseBody
import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.data.pojo.response.SpaceList
import com.netdata.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel: ViewModel() {

    val baseUrl = "https://testing.netdata.cloud/"

    init {
        NetworkClient.setBaseUrl(baseUrl)
    }

    val magicLinkLiveData = MutableLiveData<MyResponseBody<Any>>()
    val linkDeviceLiveData = MutableLiveData<MyResponseBody<Any>>()
    val unlinkDeviceLiveData = MutableLiveData<MyResponseBody<Any>>()
    val spaceListLiveData = MutableLiveData<MyResponseBody<ArrayList<SpaceList>>>()
    val fetchHomeNotificationLiveData = MutableLiveData<MyResponseBody<ArrayList<HomeNotificationList>>>()
    val listSpaceAlertCountLiveData = MutableLiveData<MyResponseBody<Any>>()
    val listActiveAlertPerRoomLiveData = MutableLiveData<MyResponseBody<Any>>()
    val getAlertDetailedLiveData = MutableLiveData<MyResponseBody<Any>>()
    val getAccountNotificationsSettingsLiveData = MutableLiveData<MyResponseBody<Any>>()
    val updateAccountNotificationsSettingsLiveData = MutableLiveData<MyResponseBody<Any>>()
    val silenceSpaceLiveData = MutableLiveData<MyResponseBody<Any>>()
    val unsilenceSpaceLiveData = MutableLiveData<MyResponseBody<Any>>()

    fun callMagicLink(apiRequest: APIRequest)
    {
        val apiService = NetworkClient.createService(MainApi::class.java)


        val callApi = /*RetrofitApi.getInst().*/apiService.loginData(apiRequest)
        callApi.enqueue(object: Callback<Any>{


            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                magicLinkLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("Fail Magic", call.toString())
                magicLinkLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
            }

        })
    }

    fun callLinkDevice(apiRequest: APIRequest)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.linkDevice(cookie, apiRequest)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.e("code", response.code().toString())
                linkDeviceLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                linkDeviceLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail link", call.toString())
            }

        })
    }

    fun callUnlinkDevice()
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.unlinkDevice(cookie)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                unlinkDeviceLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                unlinkDeviceLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail unlink", call.toString())
            }

        })
    }

    fun callGetSpaceList()
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.getSpaceList(cookie)
        callApi.enqueue(object: Callback<ArrayList<SpaceList>>{

            override fun onResponse(call: Call<ArrayList<SpaceList>>, response: Response<ArrayList<SpaceList>>) {
                spaceListLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<ArrayList<SpaceList>>, t: Throwable) {
                spaceListLiveData.postValue(MyResponseBody(0, "Test", ArrayList(),isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callFetchHomeNotification()
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.fetchHomeNotification(cookie)
        callApi.enqueue(object: Callback<ArrayList<HomeNotificationList>>{

            override fun onResponse(call: Call<ArrayList<HomeNotificationList>>, response: Response<ArrayList<HomeNotificationList>>) {
                fetchHomeNotificationLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<ArrayList<HomeNotificationList>>, t: Throwable) {
                fetchHomeNotificationLiveData.postValue(MyResponseBody(0, "Test", ArrayList(),isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callListSpaceAlertCount(spaceID: String)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.listSpaceAlertCount(cookie, spaceID)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                listSpaceAlertCountLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                listSpaceAlertCountLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callListActiveAlertPerRoom(spaceID: String, roomID: String)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.listActiveAlertPerRoom(cookie, spaceID, roomID)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                listActiveAlertPerRoomLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                listActiveAlertPerRoomLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callGetAlertDetailed(spaceID: String, roomID: String, alarm: String, chart: String)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.getAlertDetailed(cookie, spaceID, roomID, alarm, chart)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                getAlertDetailedLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                getAlertDetailedLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callGetAccountNotificationsSettings()
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.getAccountNotificationsSettings(cookie)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                getAccountNotificationsSettingsLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                getAccountNotificationsSettingsLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callUpdateAccountNotificationsSettings(apiRequest: APIRequest)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.updateAccountNotificationsSettings(cookie, apiRequest)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                updateAccountNotificationsSettingsLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                updateAccountNotificationsSettingsLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callSilenceSpace(spaceID: String, apiRequest: APIRequest)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.silenceSpace(cookie, spaceID, apiRequest)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                silenceSpaceLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                silenceSpaceLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }

    fun callUnsilenceSpace(spaceID: String, apiRequest: APIRequest)
    {
        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"

        val cookie = "$sessionId;$token"

        val apiService = NetworkClient.createService(MainApi::class.java)

        val callApi = /*RetrofitApi.getInst().*/apiService.unsilenceSpace(cookie, spaceID, apiRequest)
        callApi.enqueue(object: Callback<Any>{

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                unsilenceSpaceLiveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                unsilenceSpaceLiveData.postValue(MyResponseBody(0, "Test", "",isError = true, throwable = t))
                Log.e("Fail Space", call.toString())
            }

        })
    }
}
