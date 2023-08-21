package cloud.netdata.android.utils.customapi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.netdata.android.data.pojo.MyResponseBody
import cloud.netdata.android.data.pojo.response.DynamicLink
import cloud.netdata.android.exception.CookiesHandlerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DynamicViewModel() : ViewModel() {

    val baseUrl = "https://u8647778.ct.sendgrid.net/"

    init {
        CookiesNetworkClient.setBaseUrl(baseUrl)
    }

    val liveData = MutableLiveData<MyResponseBody<DynamicLink>>()
//    val liveData = MutableLiveData<Any>()

    fun callDynamicLink(dynamicLink: String) {
        val apiService = CookiesNetworkClient.createService(MainApi::class.java)

        val callApi = apiService.fetchData(dynamicLink)
        callApi.enqueue(object : Callback<DynamicLink> {

            override fun onResponse(call: Call<DynamicLink>, response: Response<DynamicLink>) {
                liveData.postValue(MyResponseBody(response.code(), "Test", response.body(), throwable = null))
//                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<DynamicLink>, t: Throwable) {
                liveData.postValue(MyResponseBody(0, "Test", DynamicLink(),isError = true, throwable = t))

                /*if(t is CookiesHandlerError){
                    *//*val si = t.map["s_i"]
                    val sv = t.map["s_v_$si"]
                    myEdit.putString(Constant.APP_PREF_COOKIE_SI, t.map["s_i"]!!)
                    myEdit.putString(Constant.APP_PREF_COOKIE_SV, t.map["s_v_${t.map["s_i"]}"]!!)*//*
                    liveData.postValue(t)
                }*/
                Log.e("Fail Dynamic", t.localizedMessage.toString())
            }

        })
    }

}