package com.netdata.app.utils.customapi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {
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
}