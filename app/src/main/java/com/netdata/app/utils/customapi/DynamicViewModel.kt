package com.netdata.app.utils.customapi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.exception.CookiesHandlerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DynamicViewModel : ViewModel() {

    val baseUrl = "https://u8647778.ct.sendgrid.net/"

    init {
        CookiesNetworkClient.setBaseUrl(baseUrl)
    }

    val liveData = MutableLiveData<Any>()

    fun callDynamicLink(dynamicLink: String) {
        val apiService = CookiesNetworkClient.createService(MainApi::class.java)

        val callApi = apiService.fetchData(dynamicLink)
        callApi.enqueue(object : Callback<Any> {


            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val cookies = response.headers().values("set-cookie")
                Log.e("cookie", cookies.toString())
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                if(t is CookiesHandlerError){
                    t.map.contains("")
                    Log.e("map", t.map.toString())
                    t.map
                }
                Log.e("Fail", call.toString())
            }

        })
    }

}