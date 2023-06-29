package com.netdata.app.utils.customapi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netdata.app.data.pojo.request.APIRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel: ViewModel() {

    val baseUrl = "https://app.netdata.cloud/"

    init {
        NetworkClient.setBaseUrl(baseUrl)
    }

    val liveData = MutableLiveData<Any>()

    fun callMagicLink(apiRequest: APIRequest)
    {
        val apiService = NetworkClient.createService(MainApi::class.java)


        val callApi = /*RetrofitApi.getInst().*/apiService.loginData(apiRequest)
        callApi.enqueue(object: Callback<Any>{


            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("Fail", call.toString())
            }

        })
    }
}