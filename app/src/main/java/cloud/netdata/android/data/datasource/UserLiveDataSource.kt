package cloud.netdata.android.data.datasource

import cloud.netdata.android.data.pojo.DataWrapper
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.data.repository.UserRepository
import cloud.netdata.android.data.service.AuthenticationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLiveDataSource @Inject constructor(private val authenticationService: AuthenticationService) : BaseDataSource(), UserRepository {

    override suspend fun magicLink(apiRequest: APIRequest): DataWrapper<Any> {
        return execute { authenticationService.magicLink(apiRequest) }
    }

}
