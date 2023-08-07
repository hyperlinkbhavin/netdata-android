package com.netdata.app.utils.customapi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netdata.app.core.AppPreferences
import com.netdata.app.data.pojo.MyResponseBody
import com.netdata.app.data.pojo.response.DynamicLink
import com.netdata.app.exception.CookiesHandlerError
import com.netdata.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DynamicViewModel() : ViewModel() {

    val baseUrl = "https://u8647778.ct.sendgrid.net/"

    init {
        CookiesNetworkClient.setBaseUrl(baseUrl)
    }

    val liveData = MutableLiveData<MyResponseBody<DynamicLink>>()

    fun callDynamicLink(dynamicLink: String) {
        val apiService = CookiesNetworkClient.createService(MainApi::class.java)

        val callApi = apiService.fetchData(dynamicLink)
        callApi.enqueue(object : Callback<DynamicLink> {


            override fun onResponse(call: Call<DynamicLink>, response: Response<DynamicLink>) {
                liveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
            }

            override fun onFailure(call: Call<DynamicLink>, t: Throwable) {
                if(t is CookiesHandlerError){
                    liveData.postValue(MyResponseBody(0, "Test", DynamicLink(),isError = true, throwable = t))
                }
                Log.e("Fail Dynamic", call.toString())
            }

        })
    }

}