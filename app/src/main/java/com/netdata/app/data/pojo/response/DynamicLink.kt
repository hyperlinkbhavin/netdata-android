package com.netdata.app.data.pojo.response

import com.google.gson.annotations.SerializedName

data class DynamicLink(
    @SerializedName("token" ) var token : String? = null
)
