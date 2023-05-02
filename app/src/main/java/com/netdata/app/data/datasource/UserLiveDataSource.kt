package com.netdata.app.data.datasource

import com.netdata.app.data.pojo.DataWrapper
import com.netdata.app.data.pojo.User
import com.netdata.app.data.pojo.request.LoginRequest
import com.netdata.app.data.repository.UserRepository
import com.netdata.app.data.service.AuthenticationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLiveDataSource @Inject constructor(private val authenticationService: AuthenticationService) : BaseDataSource(), UserRepository {

    override suspend fun login(request: LoginRequest): DataWrapper<User> {
        return execute { authenticationService.login(request) }
    }

}
