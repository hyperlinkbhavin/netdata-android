package cloud.netdata.android.data.repository

import cloud.netdata.android.data.pojo.DataWrapper
import cloud.netdata.android.data.pojo.request.APIRequest

interface UserRepository {
    /**
     * Same name of API nca
     */
    suspend fun magicLink(apiRequest: APIRequest): DataWrapper<Any>

}