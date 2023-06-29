package com.netdata.app.utils.customapi

import com.netdata.app.data.pojo.request.APIRequest
import retrofit2.Call
import retrofit2.http.*

interface MainApi {

    @POST("api/v2/auth/account/magic-link")
    fun loginData(@Body apiRequest: APIRequest):Call<Any>

    @GET("ls/click")
    fun fetchData(@Query("upn") dynamicLink: String): Call<Any>

}