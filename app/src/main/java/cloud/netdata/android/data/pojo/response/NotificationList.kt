package cloud.netdata.android.data.pojo.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationList(
    @SerializedName("page"        ) var page       : Int?               = null,
    @SerializedName("page_size"   ) var pageSize   : Int?               = null,
    @SerializedName("total_count" ) var totalCount : Int?               = null,
    @SerializedName("results"     ) var results    : ArrayList<Results> = arrayListOf()
): Parcelable {
    @Parcelize
    data class Results (

        @SerializedName("id"          ) var id         : Int?     = null,
        @SerializedName("create_time" ) var createTime : String?  = null,
        @SerializedName("tag"         ) var tag        : String?  = null,
        @SerializedName("title"       ) var title      : String?  = null,
        @SerializedName("message"     ) var message    : String?  = null,
        @SerializedName("model_id"    ) var modelId    : String?  = null,
        @SerializedName("is_public"   ) var isPublic   : Boolean? = null,
        @SerializedName("is_read"     ) var isRead     : Boolean? = null,
        @SerializedName("receiver"    ) var receiver   : String?  = null,
        @SerializedName("sender"      ) var sender     : String?  = null

    ): Parcelable
}
