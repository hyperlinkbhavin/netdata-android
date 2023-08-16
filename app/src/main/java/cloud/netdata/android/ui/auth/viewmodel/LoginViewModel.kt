package cloud.netdata.android.ui.auth.viewmodel

import androidx.lifecycle.viewModelScope
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.data.repository.UserRepository
import cloud.netdata.android.ui.base.APILiveData
import cloud.netdata.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val magicLinkLiveData = APILiveData<Any>()

    fun magicLink(apiRequest: APIRequest) {
        viewModelScope.launch {
            val result = userRepository.magicLink(apiRequest)
            magicLinkLiveData.value = result
        }
    }
}