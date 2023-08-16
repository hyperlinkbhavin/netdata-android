package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class OldHomeNotificationList(
    var id: Long? = 0,
    @SerializedName("@timestamp" ) var timestamp  : String?         = null,
    @SerializedName("host"       ) var host       : ArrayList<Host> = arrayListOf(),
    @SerializedName("Netdata"    ) var netData    : Netdata?        = Netdata(),
    @SerializedName("user"       ) var user       : User?           = User(),
    var isRead: Boolean = false,
    var priority: String?= null
) {
    data class Host (
        @SerializedName("id"   ) var id   : String? = null,
        @SerializedName("name" ) var name : String? = null,
        var isSelected: Boolean = false
    )

    data class Netdata (
        @SerializedName("alert"   ) var alert   : Alert?          = Alert(),
        @SerializedName("chart"   ) var chart   : Chart?          = Chart(),
        @SerializedName("space"   ) var space   : Space?          = Space(),
        @SerializedName("room"    ) var room    : ArrayList<Room> = arrayListOf(),
        @SerializedName("context" ) var context : Context?        = Context()
    ) {
        data class Alert (
            @SerializedName("name"        ) var name        : ArrayList<String> = arrayListOf(),
            @SerializedName("role"        ) var role        : ArrayList<String> = arrayListOf(),
            @SerializedName("config_hash" ) var configHash  : String?           = null,
            @SerializedName("current"     ) var current     : Current?          = Current(),
            @SerializedName("previous"    ) var previous    : Previous?         = Previous(),
            @SerializedName("class"       ) var classes       : String?           = null,
            @SerializedName("type"        ) var type        : String?           = null,
            @SerializedName("component"   ) var component   : String?           = null,
            @SerializedName("transition"  ) var transition  : Transition?       = Transition(),
            @SerializedName("annotations" ) var annotations : Annotations?      = Annotations()
        ){
            data class Current (
                @SerializedName("status"       ) var status      : ArrayList<String> = arrayListOf(),
                @SerializedName("value"        ) var value       : Int?              = null,
                @SerializedName("value_string" ) var valueString : String?           = null
            )

            data class Previous (
                @SerializedName("status"       ) var status      : String? = null,
                @SerializedName("value"        ) var value       : Int?    = null,
                @SerializedName("value_string" ) var valueString : String? = null
            )

            data class Transition (
                @SerializedName("id" ) var id : String? = null
            )

            data class Annotations (
                @SerializedName("info"         ) var info        : String? = null,
                @SerializedName("conf_source"  ) var confSource  : String? = null,
                @SerializedName("calc_expr"    ) var calcExpr    : String? = null,
                @SerializedName("edit_command" ) var editCommand : String? = null,
                @SerializedName("edit_line"    ) var editLine    : String? = null
            )
        }

        data class Chart (
            @SerializedName("id"   ) var id   : String? = null,
            @SerializedName("name" ) var name : String? = null
        )

        data class Space (
            @SerializedName("id"   ) var id   : String? = null,
            @SerializedName("name" ) var name : String? = null
        )

        data class Room (
            @SerializedName("id"   ) var id   : String? = null,
            @SerializedName("name" ) var name : String? = null
        )

        data class Context (
            @SerializedName("name" ) var name : ArrayList<String> = arrayListOf()
        )
    }

    data class User (
        @SerializedName("id"    ) var id    : String? = null,
        @SerializedName("name"  ) var name  : String? = null,
        @SerializedName("email" ) var email : String? = null
    )
}
