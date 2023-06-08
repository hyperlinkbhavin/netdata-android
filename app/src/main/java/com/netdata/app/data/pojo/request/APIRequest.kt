package com.netdata.app.data.pojo.request

import com.google.gson.annotations.SerializedName

data class APIRequest(
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("redirectURI")
    var redirectURI: String? = null,

    @SerializedName("registerURI")
    var registerURI: String? = null,
)