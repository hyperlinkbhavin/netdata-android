package com.netdata.app.data.pojo.response

import com.google.gson.annotations.SerializedName

data class NotificationPriorityList(
    var id             : Int?           = null,
    var priority       : String?           = null,
    var isSound        : Int           = 0,
    var soundName      : String?           = null,
    var soundUrl       : String?           = null,
    var isBanner       : Int           = 0,
    var isVibration    : Int           = 0,
)
