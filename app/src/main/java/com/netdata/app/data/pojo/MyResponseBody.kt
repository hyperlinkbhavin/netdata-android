package com.netdata.app.data.pojo

import com.google.gson.annotations.SerializedName

data class MyResponseBody<T>(@SerializedName("code") val responseCode: Int,
                             @SerializedName("message") val message: String,
                             @SerializedName("data") val data: T?,
val isError: Boolean = false, val throwable: Throwable?) {

}