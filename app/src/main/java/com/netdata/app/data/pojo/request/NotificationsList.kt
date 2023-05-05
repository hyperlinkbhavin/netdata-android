package com.netdata.app.data.pojo.request

import com.netdata.app.data.pojo.enumclass.Priority

data class NotificationsList(var spaceName: String, var priorityPercent: String, var dateTime: String, var priority: Priority)
