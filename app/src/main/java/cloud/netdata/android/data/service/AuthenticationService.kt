package cloud.netdata.android.data.service

import cloud.netdata.android.data.URLFactory
import cloud.netdata.android.data.pojo.ResponseBody
import cloud.netdata.android.data.pojo.request.APIRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    /**
     * API calling url and method
     */
    @POST(URLFactory.Method.MAGIC_LINK)
    suspend fun magicLink(@Body apiRequest: APIRequest): ResponseBody<Any>

}