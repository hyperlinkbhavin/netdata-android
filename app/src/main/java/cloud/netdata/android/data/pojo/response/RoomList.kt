package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class RoomList(
    @SerializedName("id"           ) var id          : String?           = null,
    @SerializedName("slug"         ) var slug        : String?           = null,
    @SerializedName("name"         ) var name        : String?           = null,
    @SerializedName("description"  ) var description : String?           = null,
    @SerializedName("untouchable"  ) var untouchable : Boolean?          = null,
    @SerializedName("private"      ) var private     : Boolean?          = null,
    @SerializedName("createdAt"    ) var createdAt   : String?           = null,
    @SerializedName("isMember"     ) var isMember    : Boolean?          = null,
    @SerializedName("node_count"   ) var nodeCount   : Int?              = null,
    @SerializedName("member_count" ) var memberCount : Int?              = null,
    @SerializedName("permissions"  ) var permissions : ArrayList<String> = arrayListOf(),
    var isSelected: Boolean = false
)
