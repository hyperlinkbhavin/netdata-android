package cloud.netdata.android.data.pojo.request

import cloud.netdata.android.data.pojo.enumclass.Priority

data class NotificationsList(var spaceName: String, var priorityPercent: String, var dateTime: String, var priority: Priority)
