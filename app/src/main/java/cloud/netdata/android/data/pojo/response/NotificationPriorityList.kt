package cloud.netdata.android.data.pojo.response

data class NotificationPriorityList(
    var id             : Int?           = null,
    var priority       : String?           = null,
    var isSound        : Int           = 0,
    var soundName      : String?           = null,
    var soundUrl       : String?           = null,
    var isBanner       : Int           = 0,
    var isVibration    : Int           = 0,
)
