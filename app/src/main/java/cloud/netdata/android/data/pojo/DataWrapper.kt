package cloud.netdata.android.data.pojo

data class DataWrapper<T>(val responseBody: ResponseBody<T>?, val throwable: Throwable?)