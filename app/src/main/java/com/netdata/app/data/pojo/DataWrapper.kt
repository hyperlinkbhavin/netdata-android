package com.netdata.app.data.pojo

data class DataWrapper<T>(val responseBody: ResponseBody<T>?, val throwable: Throwable?)