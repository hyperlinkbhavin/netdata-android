package cloud.netdata.android.data.datasource

import cloud.netdata.android.data.pojo.DataWrapper
import cloud.netdata.android.data.pojo.ResponseBody
import cloud.netdata.android.exception.ServerException

open class BaseDataSource {

    suspend fun <T> execute(callAPI: suspend () -> ResponseBody<T>): DataWrapper<T> {
        return try {
            val result = callAPI()

            when (result.responseCode) {
                0, 2, 3, 4, 11 -> {
                    DataWrapper(result, ServerException(result.message, result.responseCode))
                }
                else -> {
                    DataWrapper(result, null)
                }
            }
        } catch (e: Throwable) {
            DataWrapper(null, e)
        }
    }

}