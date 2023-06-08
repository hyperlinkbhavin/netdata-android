package com.netdata.app.data.service

import com.netdata.app.data.URLFactory
import com.netdata.app.data.pojo.ResponseBody
import com.netdata.app.data.pojo.User
import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.data.pojo.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    /**
     * API calling url and method
     */
    @POST(URLFactory.Method.MAGIC_LINK)
    suspend fun magicLink(@Body apiRequest: APIRequest): ResponseBody<Any>

}