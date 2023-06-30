package com.netdata.app.utils.customapi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netdata.app.core.AppPreferences
import com.netdata.app.exception.CookiesHandlerError
import com.netdata.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DynamicViewModel() : ViewModel() {

    /*private val appPreferences: SharedPreferences = .getSharedPreferences(AppPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    var myEdit: SharedPreferences.Editor = appPreferences.edit()*/

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
                /*val cookies = response.headers().values("set-cookie")
                Log.e("cookie", cookies.toString())*/
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                if(t is CookiesHandlerError){
                    /*val si = t.map["s_i"]
                    val sv = t.map["s_v_$si"]*/
                    /*myEdit.putString(Constant.APP_PREF_COOKIE_SI, t.map["s_i"]!!)
                    myEdit.putString(Constant.APP_PREF_COOKIE_SV, t.map["s_v_${t.map["s_i"]}"]!!)*/
                    liveData.postValue(t)
                }
                Log.e("Fail Dynamic", call.toString())
            }

        })
    }

}