package com.netdata.app.data.pojo.response

data class MaintenanceMode(
    var spaceId: String? = null,
    var spaceName: String? = null,
    var isSelected: Boolean = false,
    var isForever: Boolean = false,
    var isUntil: Boolean = false,
    var untilDate: String? = null
)
