package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class DuplicateHomeNotificationList(
    var id: Long? = 0,
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("createdAt") var createdAt: String? = null,
    var isRead: Boolean = false,
    var priority: String?= null
) {
    data class Data(
        @SerializedName("node") var node: Node? = Node(),
        @SerializedName("room") var room: Room? = Room(),
        @SerializedName("user") var user: User? = User(),
        @SerializedName("alarm") var alarm: Alarm? = Alarm(),
        @SerializedName("rooms") var rooms: ArrayList<Rooms> = arrayListOf(),
        @SerializedName("space") var space: Space? = Space(),
        @SerializedName("issuer") var issuer: String? = null,
        @SerializedName("rate_limit") var rateLimit: RateLimit? = RateLimit(),
        @SerializedName("passwordless") var passwordless: String? = null,
        @SerializedName("channel_disabled") var channelDisabled: String? = null
    ) {
        data class Node(
            @SerializedName("id") var id: String? = null,
            @SerializedName("hostname") var hostname: String? = null,
            @SerializedName("reachable") var reachable: Boolean? = null,
            var isSelected: Boolean = false
        )

        data class User(
            @SerializedName("id") var id: String? = null,
            @SerializedName("email") var email: String? = null,
            @SerializedName("name") var name: String? = null,
            @SerializedName("MobileAppToken") var MobileAppToken: String? = null
        )

        data class Space(
            @SerializedName("id") var id: String? = null,
            @SerializedName("slug") var slug: String? = null,
            @SerializedName("name") var name: String? = null
        )

        data class Room(
            @SerializedName("id") var id: String? = null,
            @SerializedName("slug") var slug: String? = null,
            @SerializedName("name") var name: String? = null
        )

        data class Rooms(
            @SerializedName("id") var id: String? = null,
            @SerializedName("slug") var slug: String? = null,
            @SerializedName("name") var name: String? = null
        )

        data class RateLimit(
            @SerializedName("threshold") var threshold: Long? = null,
            @SerializedName("window") var window: Long? = null
        )

        data class Alarm(
            @SerializedName("name") var name: String? = null,
            @SerializedName("chart") var chart: String? = null,
            @SerializedName("transition_id") var transitionId: String? = null,
            @SerializedName("chart_context") var chartContext: String? = null,
            @SerializedName("family") var family: String? = null,
            @SerializedName("status") var status: String? = null,
            @SerializedName("prev_status") var prevStatus: String? = null,
            @SerializedName("value_with_units") var valueWithUnits: String? = null,
            @SerializedName("details") var details: String? = null,
            @SerializedName("when") var whenData: String? = null,
            @SerializedName("duration") var duration: Long? = null,
            @SerializedName("non_clear_duration") var nonClearDuration: Long? = null,
            @SerializedName("classification") var classification: String? = null,
            @SerializedName("role") var role: String? = null,
            @SerializedName("raised_by") var raisedBy: String? = null,
            @SerializedName("edit_command") var editCommand: String? = null,
            @SerializedName("edit_line") var editLine: Long? = null,
            @SerializedName("warning_count") var warningCount: Long? = null,
            @SerializedName("critical_count") var criticalCount: Long? = null,
            @SerializedName("conf_file") var confFile: String? = null,
            @SerializedName("calc_expr") var calcExpr: String? = null,
            @SerializedName("log") var log: ArrayList<Log> = arrayListOf(),
            var isClassSelected: Boolean = false,
            var isTypeSelected: Boolean = false
        ) {
            data class Log(
                @SerializedName("alarm_name") var alarmName: String? = null,
                @SerializedName("status") var status: String? = null,
                @SerializedName("when") var whenData: String? = null
            )
        }
    }
}
