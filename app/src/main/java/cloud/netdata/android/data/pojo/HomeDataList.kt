package cloud.netdata.android.data.pojo

data class HomeDataList(
    var name: String,
    var dateTime: String,
    var gke: String,
    var diskSpace: String,
    var warRooms: String,
    var typeComponent: String,
    var isRead: Boolean = false
)
