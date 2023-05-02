package com.netdata.app.ui.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.netdata.app.data.pojo.User
import com.netdata.app.data.pojo.request.LoginRequest
import com.netdata.app.data.repository.UserRepository
import com.netdata.app.ui.base.APILiveData
import com.netdata.app.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val loginLiveData = APILiveData<User>()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            val result = userRepository.login(request)
            loginLiveData.value = result
        }
    }
}