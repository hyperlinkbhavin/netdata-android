package com.netdata.app.utils.customapi

import android.util.Log
import com.netdata.app.exception.CookiesHandlerError
import com.netdata.app.utils.customapi.NetworkClient.httpLogger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

/*class RetrofitApi {
    private val BASE_URL = "https://app.netdata.cloud/"
    var apiService: MainApi

    companion object{
        private var INSTANCE: RetrofitApi? = null
        fun getInst(): RetrofitApi
        {
            if(INSTANCE == null)
            {
                INSTANCE = RetrofitApi()
            }
            return INSTANCE!!
        }
    }

    init {
        val httpLogger = HttpLoggingInterceptor()
        httpLogger.level = HttpLoggingInterceptor.Level.BODY
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(httpLogger).build())
            .addConverterFactory(GsonConverterFactory.create()).build()
        apiService = retrofit.create(MainApi::class.java)
    }
}*/

object CookiesNetworkClient {
    private var baseUrl: String = ""

    val httpLogger = HttpLoggingInterceptor()

    private val okHttpClient: OkHttpClient by lazy {
        httpLogger.level = HttpLoggingInterceptor.Level.BODY
        val cookieHandler=CookieJarImpl()
        OkHttpClient.Builder()
            .cookieJar(cookieHandler)
            .addInterceptor(httpLogger)
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                cookieHandler.clearStoreCookies()
                val response = chain.proceed(request)

                Log.e("dynamic response code", response.toString())

                if(response.code == 200){
                    throw  CookiesHandlerError("Cookie Error", cookieHandler.storeCookies)
                }

                if (!response.isSuccessful) {
                    // Get cookies from the failure response headers
                    val cookies = response.headers.values("set-cookie")

                    // Loop through the cookies if there are multiple
                    Log.e("cooo", cookies.toString())
                    for (cookie in cookies) {
                        // Handle the cookie as needed
                        // Example: Print the cookie value
                        println(cookie)
                    }
                }

                response
            })
//            .addInterceptor(httpLogger)
            .build()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofitBuilder.baseUrl(baseUrl).build().create(serviceClass)
    }
}
