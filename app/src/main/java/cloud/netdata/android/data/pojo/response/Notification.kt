package cloud.netdata.android.data.pojo.response

import com.google.gson.annotations.SerializedName

data class Notification(

    @SerializedName("receiver")
    var receiver: String? = null,

    @SerializedName("receiver_id")
    var receiverId: String? = null,

    @SerializedName("receiver_type")
    var receiverType: String? = null,

    @SerializedName("sender_id")
    var senderId: String? = null,

    @SerializedName("tag")
    var tag: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("sender_type")
    var senderType: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("notification_tag")
    var notificationTag: String? = null
)
