package com.netdata.app.data.repository

import com.netdata.app.data.pojo.DataWrapper
import com.netdata.app.data.pojo.User
import com.netdata.app.data.pojo.request.APIRequest
import com.netdata.app.data.pojo.request.LoginRequest

interface UserRepository {
    /**
     * Same name of API nca
     */
    suspend fun magicLink(apiRequest: APIRequest): DataWrapper<Any>

}